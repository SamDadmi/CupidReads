let lastMessageTime = null;
let isPolling = false;
let pollInterval = 2000; // Poll every 2 seconds
let consecutiveErrors = 0;
let displayedMessageIds = new Set();

function startPolling() {
    if (!isPolling) {
        isPolling = true;
        pollMessages();
    }
}

function stopPolling() {
    isPolling = false;
}

function pollMessages() {
    if (!isPolling) return;

    const clubName = document.getElementById('clubName').value;
    let url = `/api/chat/messages?clubName=${encodeURIComponent(clubName)}`;
    
    // Only add lastMessageTime if it's not in the future
    const now = new Date();
    if (lastMessageTime && lastMessageTime <= now) {
        url += `&lastMessageTime=${lastMessageTime.toISOString()}`;
    }

    fetch(url)
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text || 'Failed to fetch messages');
                });
            }
            consecutiveErrors = 0;
            return response.json();
        })
        .then(messages => {
            if (messages && Array.isArray(messages)) {
                let hasNewMessages = false;
                messages.forEach(message => {
                    if (message && message.id && !displayedMessageIds.has(message.id)) {
                        showMessage(message);
                        displayedMessageIds.add(message.id);
                        hasNewMessages = true;
                        
                        if (message.timestamp) {
                            const messageTime = new Date(message.timestamp);
                            if (!lastMessageTime || messageTime > lastMessageTime) {
                                lastMessageTime = messageTime;
                            }
                        }
                    }
                });
                
                if (hasNewMessages) {
                    const messageArea = document.getElementById('chat-messages');
                    messageArea.scrollTop = messageArea.scrollHeight;
                }
            }
        })
        .catch(error => {
            console.error('Error polling messages:', error);
            consecutiveErrors++;
            showError(error.message || 'Error fetching messages. Retrying...');
        })
        .finally(() => {
            if (isPolling) {
                setTimeout(pollMessages, pollInterval);
            }
        });
}

function sendMessage(message) {
    const clubName = document.getElementById('clubName').value;
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch('/api/chat/send', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            [csrfHeader]: csrfToken
        },
        body: `message=${encodeURIComponent(message)}&clubName=${encodeURIComponent(clubName)}`
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(text || 'Failed to send message');
            });
        }
        // Force an immediate poll after sending a message
        pollMessages();
    })
    .catch(error => {
        console.error('Error sending message:', error);
        showError(error.message || 'Failed to send message. Please try again.');
    });
}

function showMessage(message) {
    if (!message || !message.id) return;
    
    // Check if message is already displayed
    if (document.querySelector(`[data-message-id="${message.id}"]`)) {
        return;
    }

    const messageElement = document.createElement('div');
    messageElement.classList.add('message');
    messageElement.setAttribute('data-message-id', message.id);
    
    const isCurrentUser = message.user && message.user.username === currentUsername;
    messageElement.classList.add(isCurrentUser ? 'sent' : 'received');
    
    const timestamp = message.timestamp ? new Date(message.timestamp).toLocaleTimeString() : '';
    const content = message.message || '';
    
    messageElement.innerHTML = `
        <div class="message-header">
            ${message.user ? `<span class="username">${message.user.username}</span>` : ''}
            <span class="timestamp">${timestamp}</span>
        </div>
        <div class="message-content">${content}</div>
    `;
    
    const messageArea = document.getElementById('chat-messages');
    messageArea.appendChild(messageElement);
}

function showError(message) {
    const errorElement = document.createElement('div');
    errorElement.classList.add('alert', 'alert-error');
    errorElement.textContent = message;
    
    const container = document.querySelector('.container');
    const existingError = container.querySelector('.alert-error');
    if (existingError) {
        existingError.remove();
    }
    
    container.insertBefore(errorElement, document.getElementById('chat-messages'));
    
    setTimeout(() => {
        if (errorElement.parentNode === container) {
            errorElement.remove();
        }
    }, 5000);
}

// Initialize when the page loads
document.addEventListener('DOMContentLoaded', () => {
    // Start polling for messages
    startPolling();
    
    // Handle form submission
    const messageForm = document.getElementById('messageForm');
    messageForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const messageInput = document.getElementById('message');
        const message = messageInput.value.trim();
        
        if (message) {
            sendMessage(message);
            messageInput.value = '';
        }
    });
    
    // Stop polling when leaving the page
    window.addEventListener('beforeunload', () => {
        stopPolling();
    });
}); 