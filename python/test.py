import cv2
import os

def filterVideo(video_path, image_path, strength):
    """
    Apply mask to video
    """
    # Load the mask image
    mask = cv2.imread(image_path)
    if mask is None:
        print(f"Error: Unable to load image at {image_path}")
        return

    # Open the video file
    cap = cv2.VideoCapture(video_path)
    if not cap.isOpened():
        print(f"Error: Unable to open video at {video_path}")
        return

    # Get video properties
    frame_width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
    frame_height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
    fps = cap.get(cv2.CAP_PROP_FPS)

    # Resize the mask to match the video frame size
    mask = cv2.resize(mask, (frame_width, frame_height))

    # Prepare the output video writer
    base_name = os.path.basename(video_path)
    output_name_webm = base_name.replace(".webm", "_masked.webm")
    output_path_webm = os.path.join(os.path.dirname(video_path), output_name_webm)

    fourcc_webm = cv2.VideoWriter_fourcc(*'VP80')
    out_webm = cv2.VideoWriter(output_path_webm, fourcc_webm, fps, (frame_width, frame_height))
    total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
    current_frame = 0
    # Process each frame
    while cap.isOpened():
        ret, frame = cap.read()
        if not ret:
            break
        progress = current_frame / total_frames * 100
        print(f"Processing: {progress:.2f}%")
        # Apply the weighted mask
        blended_frame = cv2.addWeighted(frame, 1 - float(strength), mask, float(strength), 0)
        current_frame+=1
        # Write the blended frame to the output video
        out_webm.write(blended_frame)

    # Release everything
    cap.release()
    out_webm.release()

    # Return the name of the saved video
    return output_name_webm

# Example usage:
# webm_name = filterVideo('../storage/example.webm', '../storage/mask.png', 0.5)
# print(f"Saved WebM: {webm_name}")

print(filterVideo('../storage/example.webm', '../storage/cat.png', 0.5))