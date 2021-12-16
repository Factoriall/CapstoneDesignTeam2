import cv2
import mediapipe as mp


# Mediapipe를 사용해서
# 관련 정보는 https://www.youtube.com/watch?v=brwgBf6VB0I&t=1441s 참고
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

    # 포즈의 정보를 찾아줘서 img 정보를 돌려주는 메서드, draw를 false로 설정하면 화면에서 안 그려줌
    def findPose(self, img, draw=True):
        imgRGB = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
        self.results = self.pose.process(imgRGB)
        if draw and self.results.pose_landmarks:  # 그림 그려주는 툴
            self.mpDraw.draw_landmarks(img, self.results.pose_landmarks,
                                       self.mpPose.POSE_CONNECTIONS)
        return img

    # 각각 joint의 정보를 lmList로 정리해서 돌려주는 메서드, draw를 false로 설정하면 원 안 그려줌
    def findPosition(self, img, draw=True):
        lmList = []
###        print("lmList start")
        if self.results.pose_landmarks:
            for id, lm in enumerate(self.results.pose_landmarks.landmark):
                h, w, c = img.shape
#                print(id, lm)
                cx, cy = int(lm.x * w), int(lm.y * h)
                lmList.append([id, lm.x, lm.y, lm.z])
                if draw:
                    cv2.circle(img, (cx, cy), 5, (255, 0, 0), cv2.FILLED)  # 원 그리기
        return lmList


def main():
    return


if __name__ == "__main__":
    main()
