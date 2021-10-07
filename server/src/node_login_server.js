//const mysql = require('mysql');
const express = require('express');
const bodyParser = require('body-parser');
var app = express();
const Logger = require('./node_core_logger');

//로그인 및 회원가입 구현 파트
class NodeLoginServer {
    constructor(config) {
        this.port = config.login.port 
        app.use(bodyParser.json());
        app.use(bodyParser.urlencoded({extended: true}));

        /*
        app.post('/user/login', function (req, res) {
            console.log(req.body);
            var userEmail = req.body.userEmail;
            var userPwd = req.body.userPwd;
            var sql = 'select * from Users where UserEmail = ?';
        
            connection.query(sql, userEmail, function (err, result) {
                var resultCode = 404;
                var message = '에러가 발생했습니다';
        
                if (err) {
                    console.log(err);
                } else {
                    if (result.length === 0) {
                        resultCode = 204;
                        message = '존재하지 않는 계정입니다!';
                    } else if (userPwd !== result[0].UserPwd) {
                        resultCode = 204;
                        message = '비밀번호가 틀렸습니다!';
                    } else {
                        resultCode = 200;
                        message = '로그인 성공! ' + result[0].UserName + '님 환영합니다!';
                    }
                }
        
                res.json({
                    'code': resultCode,
                    'message': message
                });
            })
        });*/
    }

    run() {
        app.listen(this.port, () => {
            Logger.log(`Node Login Http Server started on port: ${this.port}`);
        });
    }
}

module.exports = NodeLoginServer;