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

import os
import string
import random
import requests
from flask import Flask, jsonify, request
from moviepy.editor import VideoFileClip
from PIL import Image
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

sessions = {
    "test": {
        "media_id": 5007 
    }
}

def generate_random_string(length = 10):
    """
    Generating id
    """

    # Define the characters you want to use in your random string
    characters = string.ascii_letters + string.digits

    # Generate a random string of specified length
    random_string = ''.join(random.choice(characters) for _ in range(length))
    
    return random_string

@app.route("/initial", methods=['GET'])
def initial():
    """
    IDK)
    """

    session_id = request.args.get("sessionid")

    if(session_id not in sessions):
        return {"error": "no session with this id"}
    
    # print(sessions[session_id])

    response = requests.get("http://localhost:8080/api/videos/"+str(sessions[session_id]['media_id']))

    return response.json()


@app.route("/start", methods=['GET'])
def start():
    """
    Method for starting editor session

    Returns:
        Response: A JSON response containing sesion id
    """

    media_id = request.args.get('id')
    id_session = generate_random_string() 

    sessions[id_session] = {
        "id": id_session,
        "media_id": media_id
    }

    return sessions[id_session]

@app.route('/prepare', methods=['GET'])
def prepare_video():
    """
    Change initial video to right format and sizze

    Returns:
        Response: A JSON response containing path to video
    """

    # pylint: disable=W0718, E1101

    video_path = request.args.get('path')

    if not video_path:
        return jsonify({'success': False, 'error': 'Video path is missing'})

    try:
        video_directory = os.path.dirname(video_path)
        video_filename = os.path.basename(video_path)
        new_name = os.path.splitext(video_filename)[0] + '.webm'
        new_preview_name = os.path.splitext(video_filename)[0] + ".png"

        video_clip = VideoFileClip(video_path)

        resized_clip = video_clip.resize(width=1920)

        output_filepath = os.path.join(video_directory, new_name)
        resized_clip.write_videofile(output_filepath, codec='libvpx',
        ffmpeg_params=["-deadline", "realtime", "-cpu-used", "0"])

        file_stats = os.stat(output_filepath)

        sz = file_stats.st_size / (1024 * 1024)

        new_clip = VideoFileClip(output_filepath)

        preview_frame = new_clip.get_frame(1)

        preview_image = Image.fromarray(preview_frame)

        preview_image.save(os.path.join(video_directory, new_preview_name))

        return jsonify(
            {
                'success': True, 
                'message': 'Video prepared successfully', 
                'output_file': new_name,
                'fps': round(resized_clip.fps, 0),
                'size': str(round(sz, 2)) + "MB",
                'width': resized_clip.size[0],
                'height': resized_clip.size[1],
                'duration': resized_clip.duration,
                'preview': new_preview_name
                }
            )

    except Exception as e:
        print(e)
        return jsonify({'error': 'An error occurred: ' + str(e)})
if __name__ == '__main__':
    app.run(debug=True)
