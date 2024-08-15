// Check if a session exists
function checkSession() {
    fetch('../../CheckSession') // Call the CheckSession servlet
        .then(response => {
            if (response.ok) {
                // Session exists, do nothing
                console.log('Session exists');
            } else {
                // Session doesn't exist, redirect to index.html
                console.log('Session does not exist, redirecting...');
                window.location.href = '../../index.html';
            }
        })
        .catch(error => {
            console.error('Error checking session:', error);
        });
}

// Call the checkSession function when the page loads
window.onload = checkSession;