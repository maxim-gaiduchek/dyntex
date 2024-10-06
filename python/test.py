import cv2
import os
import numpy as np

def applyMask(background, overlay, strength):
    # separate the alpha channel from the color channels
    alpha_channel = overlay[:, :, 3] / 255 # convert from 0-255 to 0.0-1.0
    overlay_colors = overlay[:, :, :3]

    # To take advantage of the speed of numpy and apply transformations to the entire image with a single operation
    # the arrays need to be the same shape. However, the shapes currently looks like this:
    #    - overlay_colors shape:(width, height, 3)  3 color values for each pixel, (red, green, blue)
    #    - alpha_channel  shape:(width, height, 1)  1 single alpha value for each pixel
    # We will construct an alpha_mask that has the same shape as the overlay_colors by duplicate the alpha channel
    # for each color so there is a 1:1 alpha channel for each color channel
    alpha_mask = np.dstack((alpha_channel, alpha_channel, alpha_channel))
    adjusted_alpha_mask = alpha_mask * strength

    # The background image is larger than the overlay so we'll take a subsection of the background that matches the
    # dimensions of the overlay.
    # NOTE: For simplicity, the overlay is applied to the top-left corner of the background(0,0). An x and y offset
    # could be used to place the overlay at any position on the background.
    h, w = overlay.shape[:2]
    # print(h, w)
    # print("dads")
    background_subsection = background[0:h, 0:w]

    # combine the background with the overlay image weighted by alpha
    composite = background_subsection * (1 - adjusted_alpha_mask) + overlay_colors * adjusted_alpha_mask

    # overwrite the section of the background image that has been updated
    background[0:h, 0:w] = composite

    return background
def filterVideo(video_path, image_path, strength, name):
    """
    Apply mask to video
    """
    # Load the mask image
    video_path = "../storage/" + video_path
    image_path = "../storage/" + image_path
    print(video_path, image_path)
    mask = cv2.imread(image_path, cv2.IMREAD_UNCHANGED)
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
    os.path.basename(video_path)
    output_name_webm = name.replace(".png", ".webm")
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
        # blended_frame = cv2.addWeighted(frame, 1 - float(strength), mask, float(strength), 0)
        blended_frame = applyMask(frame, mask, float(strength))
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

print(filterVideo('LFitaWz3WF.webm', 'mask_20240521_115551_8426.png', 0.9, "emaxmple_masked.png"))