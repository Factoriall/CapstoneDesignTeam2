import cv2
import mediapipe as mp


# pose detection using mediapipe
# https://www.youtube.com/watch?v=brwgBf6VB0I&t=1441s
class poseDetector():
    def __init__(self,
                 mode=False,
                 upBody=False,
                 smooth=True,
                 detectionCon=0.5,
                 trackCon=0.5):
        self.mode = mode
        self.upBody = upBody
        self.smooth = smooth
        self.detectionCon = detectionCon
        self.trackCon = trackCon

        self.mpDraw = mp.solutions.drawing_utils
        self.mpPose = mp.solutions.pose
        self.pose = self.mpPose.Pose(self.mode, self.upBody, self.smooth,
                                     self.detectionCon, self.trackCon)

    #find pose, return img
    def findPose(self, img, draw=True):
        imgRGB = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        self.results = self.pose.process(imgRGB)
        if draw and self.results.pose_landmarks:  # img drawing
            self.mpDraw.draw_landmarks(img, self.results.pose_landmarks,
                                       self.mpPose.POSE_CONNECTIONS)
        return img

    # return joint info in lmlist
    def findPosition(self, img, draw=True):
        lmList = []
        if self.results.pose_landmarks:
            for id, lm in enumerate(self.results.pose_landmarks.landmark):
                h, w, c = img.shape
                cx, cy = int(lm.x * w), int(lm.y * h)
                lmList.append([id, lm.x, lm.y, lm.z])
                if draw:
                    cv2.circle(img, (cx, cy), 5, (255, 0, 0), cv2.FILLED)  # draw circle
        return lmList


def main():
    return


if __name__ == "__main__":
    main()
