from flask import Flask, jsonify, request

app = Flask(__name__)


@app.route('/', methods=['GET'])
def index():
    return jsonify({"response": "hello"})

if __name__ == '__main__':
    app.run(debug=True)

