"""
A simple Flask application that provides a basic API endpoint.

Usage:
    To run the application, execute this module directly:
        $ python app.py

    Alternatively, you can import the `app` object and run it using a WSGI server of your choice.

Example:
    Running the application:
        $ python main.py
    Accessing the API endpoint:
        $ curl http://127.0.0.1:5000/

Dependencies:
    - Flask
"""

from flask import Flask, jsonify

app = Flask(__name__)

@app.route('/', methods=['GET'])
def index():
    """
    Handle GET requests to the root endpoint.

    Returns:
        Response: A JSON response containing a greeting message.
    """
    return jsonify({"response": "hello 2"})

if __name__ == '__main__':
    app.run(debug=True)
