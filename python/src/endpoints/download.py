
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