#-*- coding:utf-8 -*-

from sys import argv

import pickle
import cv2
import numpy as np

import PoseModule as pm
import pandas as pd


# 따로 분리한 함수들
# 중요하지 않은 관절 정보 빼내기
def is_not_important(num):
    return num <= 10 or num >= 29 or 17 <= num <= 22


# data로부터 결과를 도출하기 위한 간단한 함수(현재는)
def evaluation(data):  # evaluation - 간단하게 앞,오,왼,앞,왼,오,앞,오,왼,뒤 를 본다면 성공으로 간주
    success_arr = ['f', 'r', 'l', 'f', 'l', 'r', 'f', 'r', 'l', 'b']
    sidx = 0
    score = 0
    for d_item in data:
        if sidx >= len(success_arr):
            break
        if d_item[0] == 'turn':
            continue
        if d_item[0] == success_arr[sidx]:
            score += 1
        sidx += 1

    # 점수가 8 이상이면 ret 값으로 성공 저장, 아님 실패 저장
    # 이 부분 서버에 맞춰서 간단히 조작
    if score >= 8:
        result = "success"
    else:
        result = "failure"
    return result


# 실제 시작 지점
def main(vstream):

    # pkl 파일을 해석해서 model에 저장
    with open('./body_language_dir.pkl', 'rb') as f:
        model = pickle.load(f)
    
    detector = pm.poseDetector()
    
    # cap에 비교할 영상을 넣는 부분, 이 부분을 스트리밍 변수로 대체 가능
    cap = cv2.VideoCapture(vstream)
    
    prev = "f"
    cnt = 0
    save_data = []
    loop = 0;

    # 실제 영상이 돌아가는 부분
    while True:
        loop = loop + 1;
        success, img = cap.read()
        if not success:
            break
        img = detector.findPose(img, False)
        lmList = detector.findPosition(img, draw=False)
        csvRow = []
    
        # poseDetector 활용해 관절 포인트 정보를 캐내는 부분
        # max, min 값을 이용해 scaling
        min_x = 1
        min_y = 1
        max_x = 0
        max_y = 0
#        print("img: ", end='')
#        print(img)
#        print("lmList: ", end='')
#        print(lmList)
        for joint in lmList:
#            print("lmList check")
#            print(joint)
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
#        print("csvRow: ", end='')
#        print(csvRow)
        # 실제 pkl 파일을 이용해서 체크 및 점수를 매김
        X = pd.DataFrame([csvRow])
        body_language_class = model.predict(X)[0]
        body_language_prob = model.predict_proba(X)[0]
#        print(body_language_class, body_language_prob)
    
        # 현 상태 박스를 화면에 그림
        cv2.rectangle(img, (0, 0), (250, 60), (245, 117, 16), -1)
    
        # prob는 pkl을 이용해 left, right, front, back, turn 중 어떤게 가장 맞는지를 판별
        prob = round(body_language_prob[np.argmax(body_language_prob)], 2)
        # now는 현 프레임에서 가장 prob가 높다고 판단한 자세 string
        now = body_language_class.split(' ')[0]
    
        # 확률을 cv2를 통해 visualize
        cv2.putText(img, 'PROB'
                    , (15, 12), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 0), 1, cv2.LINE_AA)
        cv2.putText(img, str(prob)
                    , (10, 40), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2, cv2.LINE_AA)
    
        # 현재 상태를 visualize
        cv2.putText(img, 'CLASS'
                    , (95, 12), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 0), 1, cv2.LINE_AA)
        cv2.putText(img, now
                    , (90, 40), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2, cv2.LINE_AA)
    
        # 예외 판별, 관련 정보가 turn이고 일정 시간보다 덜 연속될 시 무시
        if prev == now:
            cnt += 1
        else:
            if cnt >= 10:
                if len(save_data) == 0 or save_data[-1][0] != prev:
                    save_data.append([prev, cnt])
                else:
                    save_data[-1][1] += cnt
            else:
                if save_data[-1][0] == now:
                    save_data[-1][1] += cnt
            cnt = 1
            prev = now
    
#        cv2.imshow('Raw Webcam Feed', img)
#        if cv2.waitKey(10) & 0xFF == ord('q'):
#        if ord('q'):
#           break
    
    # 화면 확인 종료
    cap.release()
    # 결과를 결과.txt에 저장
    save_data.append([prev, cnt])
    with open("결과.txt", "w") as f:
        f.write("[결과값 확인]\n")
    
    with open("결과.txt", "a") as text_file:
        for item in save_data:
            text_file.write(item[0] + ": " + str(item[1]) + "\n")
    
    # evaluation 함수 통해 결과값 도출
    ret = evaluation(save_data)
    
    print(ret + ", frame: " + str(loop), end='')

if __name__ == "__main__":
    vstream = argv[1]
    main(vstream)
