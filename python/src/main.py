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
from flask import Flask, jsonify, request, send_file
from moviepy.editor import VideoFileClip
from PIL import Image
from flask_cors import CORS
import cv2

app = Flask(__name__)
CORS(app)

sessions = {
    "test": {
        "media_id": 5007 
    }
}

@app.route("/download/<path:file_path>", methods=['GET'])
def download(file_path):
    """
    Download a file from the storage directory.

    Parameters:
        file_path (str): The path to the file relative to the storage directory.

    Returns:
        Response: The file to be downloaded.
    """
    try:
        # Construct the absolute path to the file
        absolute_path = os.path.abspath(os.path.join(app.root_path, '..', '..', 'storage', file_path))
        # Check if the file exists
        if not os.path.isfile(absolute_path):
            return jsonify({'error': 'File not found'})
        
        # Send the file for download
        return send_file(absolute_path, as_attachment=True)
    except Exception as e:
        return jsonify({'error': 'An error occurred: ' + str(e)})

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

@app.route("/save", methods=['POST'])
def save():
    """
    Save video
    """

    data_res = request.get_json()
    data = data_res['data'][::-1]

    if(len(data) == 1 and data[0]['type'] != "filter"):
        session = sessions[data_res['session_id']]
        return {"status": "Ok", "link": data.pop()["path"]}

    return response.json()

@app.route("/filter", methods=['GET'])
def filter():
    """
    Applies an image onto a video.

    Parameters:
        video_path (str): The path to the video file.
        image_path (str): The path to the image file.
        strengst (int): The strength of the filter.

    Returns:
        Response: A JSON response indicating the success or failure of the operation.
    """
    video_path = "../storage/" + request.args.get('video_path').split('?')[0]
    image_path = "../storage/" + request.args.get('image_path').split('?')[0]

    strength = request.args.get('strength')
    if video_path.endswith('.png'):
        video_path, image_path = image_path, video_path
        print(float(strength))
        strength = 1 - float(strength)
        print(strength)
    name = request.args.get('name')
    if(name == ""):
        name = False

    print(strength)

    if not video_path or not image_path or not strength:
        return jsonify({'success': False, 'error': 'One or more parameters are missing'})

    try:            
        cap = cv2.VideoCapture(video_path)

        if not cap.isOpened():
            print("Error: Unable to open video file.")
            exit()

        ret, first_frame = cap.read()

        image = cv2.imread(image_path)
        if(not video_path.endswith('png') and not image_path.endswith('png')):
            cap2 = cv2.VideoCapture(image_path)
            ret2, image = cap2.read()

        # Resize the image to fit the frame size
        resized_image = cv2.resize(image, (first_frame.shape[1], first_frame.shape[0]))

        # Get the first frame of the video

        # Apply the filter to the first frame
        filtered_frame = cv2.addWeighted(first_frame, 1 - float(strength), resized_image, float(strength), 0)

        # Generate a random name for the image file
        image_filename = generate_random_string() + '.png'
        if(name):
            image_filename = name + '.png'

        # Define the path to save the image in the same directory as the video
        video_directory = os.path.dirname(video_path)
        image_path = os.path.join(video_directory, image_filename)

        # Save the image
        cv2.imwrite(image_path, filtered_frame)

        cap.release()
        if(not video_path.endswith('png') and not image_path.endswith('png')):
            cap2.release()

        # Return the path to the saved image
        return jsonify({'success': True, 'image_path': image_filename})
    except Exception as e:
        return jsonify({'error': 'An error occurred: ' + str(e)})

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
