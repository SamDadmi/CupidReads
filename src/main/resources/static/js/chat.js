let stompClient = null;
let username = null;

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        username = frame.headers['user-name'];
        
        // Subscribe to the public topic
        stompClient.subscribe('/topic/public', function(message) {
            showMessage(JSON.parse(message.body));
        });
        
        // Subscribe to user-specific messages
        stompClient.subscribe('/user/queue/reply', function(message) {
            showMessage(JSON.parse(message.body));
        });
        
        // Notify users that new user joined
        sendMessage("joined the chat!");
        
    }, function(error) {
        if(error.headers && error.headers.message) {
            console.error('Error: ' + error.headers.message);
            showError("Connection error: " + error.headers.message);
        } else {
            console.error('Error: ' + error);
            showError("Connection error. Please ensure you are logged in.");
        }
        disconnect();
    });
}

function disconnect() {
    if(stompClient !== null) {
        sendMessage("left the chat.");
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendMessage(message) {
    if(!stompClient || !stompClient.connected) {
        showError("Not connected to chat. Please try reconnecting.");
        return;
    }
    
    stompClient.send("/app/chat.send", {}, JSON.stringify({
        'content': message,
        'type': 'CHAT'
    }));
}

function showMessage(message) {
    const messageArea = document.querySelector('#messageArea');
    const messageElement = document.createElement('div');
    messageElement.classList.add('message');
    
    if(message.type === 'JOIN' || message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' ' + message.content;
    } else {
        messageElement.classList.add('chat-message');
        
        // Create message header
        const messageHeader = document.createElement('div');
        messageHeader.classList.add('message-header');
        
        // Create and add avatar
        const avatar = document.createElement('i');
        avatar.classList.add('avatar');
        avatar.textContent = message.sender[0].toUpperCase();
        messageHeader.appendChild(avatar);
        
        // Create and add username
        const usernameElement = document.createElement('span');
        usernameElement.classList.add('username');
        usernameElement.textContent = message.sender;
        messageHeader.appendChild(usernameElement);
        
        messageElement.appendChild(messageHeader);
    }

    const textElement = document.createElement('p');
    textElement.textContent = message.content;
    messageElement.appendChild(textElement);
    
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function showError(message) {
    const errorDiv = document.createElement('div');
    errorDiv.classList.add('alert', 'alert-danger');
    errorDiv.textContent = message;
    
    const messageArea = document.querySelector('#messageArea');
    messageArea.appendChild(errorDiv);
    
    setTimeout(() => {
        errorDiv.remove();
    }, 5000);
}

// Event listeners
document.addEventListener('DOMContentLoaded', function() {
    const messageForm = document.querySelector('#messageForm');
    const messageInput = document.querySelector('#message');
    
    connect();
    
    messageForm.addEventListener('submit', function(event) {
        event.preventDefault();
        const messageContent = messageInput.value.trim();
        
        if(messageContent) {
            sendMessage(messageContent);
            messageInput.value = '';
        }
    });
    
    // Handle page unload
    window.addEventListener('beforeunload', function() {
        disconnect();
    });
}); 