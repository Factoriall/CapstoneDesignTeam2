#-*_ coding:utf-8 -*-

from sys import argv

import cv2
import time
import json
import PoseModule as pm
import numpy as np
import scipy.spatial as sp


def main(vstream):
    cap = cv2.VideoCapture(vstream)
    success, img = cap.read()
    pTime = 0
    detector = pm.poseDetector()  # 모듈 공수
    loop = 0;
    while True:
        loop = loop + 1;
        success, img = cap.read()
        if not success:
            break
        img = detector.findPose(img, False)
        lmList = detector.findPosition(img, draw=False)  #joint 저장소

        cTime = time.time()
        fps = 1 / (cTime - pTime)
        pTime = cTime

        cv2.putText(img, str(int(fps)), (70, 50), cv2.FONT_HERSHEY_PLAIN, 3,
                    (255, 0, 0), 3)
#        cv2.imshow("Image", img)
#        cv2.waitkey(1)

    print("Finished: " + str(loop), end='')


if __name__ == "__main__":
    vstream = argv[1]
    main(vstream)
