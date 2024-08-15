// Function to perform user registration
function performRegister() {
    // Retrieve username, password, email, and error label element from input fields
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var email = document.getElementById("email").value;
    var errorMsgLabel = document.getElementById("errorMsgLabel");

    // Reset the error label content
    errorMsgLabel.textContent = '';

    // Send a POST request to the server for user registration
    fetch('../Register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        // Encode and send username, password, and email in the request body
        body: 'username=' + encodeURIComponent(username) + '&password=' + encodeURIComponent(password) + '&email=' + encodeURIComponent(email),
    })
    .then(response => response.text()) // Parse the response as text
    .then(data => {
        console.log(data); // Log the response data to the console
        
        // Check if registration was successful (modify this condition based on your logic)
        if (data.includes('Registration successful')) {
            // Redirect to the registerredirect.html page upon successful registration
            window.parent.location.href = 'registerredirect.html';
        }
        else {
            // Handle specific error cases
            if (data.includes('Username already exists')) {
                errorMsgLabel.textContent = 'Username already exists.';
            }
            else if (data.includes('Email already exists')) {
                errorMsgLabel.textContent = 'Email already exists.';
            }
            else {
                // Log unexpected response data for debugging purposes
                console.log('Unexpected response data:', data);
                
                // Optionally, set a generic error message
                errorMsgLabel.textContent = 'Unexpected error during registration.';
            }

            // Display the error message label
            errorMsgLabel.classList.remove('hidden');
        }
    })
    .catch(error => console.error('Error:', error)); // Log any errors that occur during the fetch operation
}