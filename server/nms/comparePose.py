from sys import argv

import pickle
import cv2
import numpy as np

import PoseModule as pm
import pandas as pd
import warnings

import time



# ignore not important joint info
def is_not_important(num):
    return num <= 10 or num >= 29 or 17 <= num <= 22


# score evaluation
def evaluation(data):  # success when all pose is correct
    success_arr = [
        "f_start",
        "r_l_knee",
        "r_r_fist",
        "l_r_knee",
        "l_l_fist",
        "f_l_knee",
        "f_r_fist",
        "l_l_body",
        "l_r_fist",
        "r_r_body",
        "r_l_fist",
        "f_r_knee",
        "f_l_fist",
        "r_l_head",
        "r_r_kick",
        "r_r_fist",
        "l_r_head",
        "l_l_kick",
        "l_l_fist",
        "b_l_knee",
        "b_r_fist"
    ]
    sidx = 0
    score = 0
	# add score when sucess array and result is same
    for d_item in data:
        if sidx >= len(success_arr):
            break
        if d_item[0] == 'turn':
            continue
        if d_item[0] == success_arr[sidx]:
            score += 1
            sidx += 1

    # success when score == 21
    with open("result.txt", "a") as text_file:
        text_file.write("score: " + str(score))
    if score == 21:
        result = "success"
    else:
        result = "failure"
    return result



# vstream: rtmp streaming link
def main(vstream):
    warnings.simplefilter('ignore', UserWarning)
    start = time.time()
    # model load using pkl file
    with open('./body_language_dir.pkl', 'rb') as f:
        model = pickle.load(f)
    detector = pm.poseDetector()

    # load streaming videos to cap
    cap = cv2.VideoCapture(vstream)

    prev = "f_start"
    cnt = 0
    save_data = []
    loop = 0

    # frame detector
    while True:
        success, img = cap.read()
        loop = loop + 1
        if not success:
            break
        img = detector.findPose(img, False)
        lmList = detector.findPosition(img, draw=False)
        csvRow = []

        # get joint info from poseDetector
        # max, min scaling
        min_x = 1
        min_y = 1
        max_x = 0
        max_y = 0
        for joint in lmList:
            if is_not_important(joint[0]):
                continue
            max_x = max(max_x, joint[1])
            max_y = max(max_y, joint[2])
            min_x = min(min_x, joint[1])
            min_y = min(min_y, joint[2])
        for joint in lmList:
            if is_not_important(joint[0]):
                continue
            csvRow.append((joint[1] - min_x) / (max_x - min_x))
            csvRow.append((joint[2] - min_y) / (max_y - min_y))
            csvRow.append(joint[3])
        if len(csvRow) == 0:  # ignore no detction
            continue

        # score check start
        X = pd.DataFrame([csvRow])
        body_language_class = model.predict(X)[0]
        body_language_prob = model.predict_proba(X)[0]

        # draw image
        cv2.rectangle(img, (0, 0), (250, 60), (245, 117, 16), -1)

        # check similar direstion using pkl file
        prob = round(body_language_prob[np.argmax(body_language_prob)], 2)
        # now -> present high probability pose 
        now = body_language_class.split(' ')[0]

        detail_language_class = ""
        detail_language_prob = ""
        if now != 'turn': # check similar pose with pkl files when not turn
            with open('body_language_dir_'+ now +'.pkl', 'rb') as f:
                detail = pickle.load(f)
            X_2 = pd.DataFrame([csvRow])
            detail_language_class = detail.predict(X_2)[0]
            detail_language_prob = detail.predict_proba(X_2)[0]
            now = now + "_" + detail_language_class.split(' ')[0]


        # visualize probability with cv2
        cv2.putText(img, 'PROB'
                    , (15, 12), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 0), 1, cv2.LINE_AA)
        cv2.putText(img, str(prob)
                    , (10, 40), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2, cv2.LINE_AA)

        # visualize present status
        cv2.putText(img, 'CLASS'
                    , (95, 12), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 0), 1, cv2.LINE_AA)
        cv2.putText(img, now
                    , (90, 40), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2, cv2.LINE_AA)

        # exception test, ignore turn and pose under specific framei
		if prev == now:
            cnt += 1
        else:
            if cnt >= 4:
                if len(save_data) == 0 or save_data[-1][0] != prev:
                    save_data.append([prev, cnt])
                else:
                    save_data[-1][1] += cnt
            else:
                if len(save_data) != 0 and save_data[-1][0] == now:
                    save_data[-1][1] += cnt
            cnt = 1
            prev = now

#        cv2.imshow('Raw Webcam Feed', img)
#        if cv2.waitKey(10) & 0xFF == ord('q'):
#            break

    # check end
    cap.release()
    cv2.destroyAllWindows()

    # save result on result.txt
    save_data.append([prev, cnt])
    with open("result.txt", "w") as f:
        f.write("[result check]\n")

    with open("result.txt", "a") as text_file:
        for item in save_data:
            text_file.write(item[0] + ": " + str(item[1]) + "\n")

    # evaluation
    ret = evaluation(save_data)
    end = time.time()

    print(ret, end='')

# main execution code
if __name__ == "__main__":
    vstream = argv[1]
    main(vstream)
