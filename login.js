// Function to perform user login
function performLogin() {
    // Retrieve username and password from input fields
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    // Send a POST request to the server for authentication
    fetch('../MyLogin', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        // Encode and send username and password in the request body
        body: 'username=' + encodeURIComponent(username) + '&password=' + encodeURIComponent(password),
    })
    .then(response => response.text()) // Parse the response as text
    .then(data => {
        console.log(data); // Log the response data to the console
        
        // Check if login was successful (modify this condition based on your logic)
        if (data.includes('User found')) {
            // Redirect to the main.html page upon successful login
            window.parent.location.href = 'secured/main.html';
        }
    })
    .catch(error => console.error('Error:', error)); // Log any errors that occur during the fetch operation
}