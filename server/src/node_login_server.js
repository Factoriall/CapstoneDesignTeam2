const mysql = require('mysql');
const express = require('express');
const bodyParser = require('body-parser');
var app = express();
const Logger = require('./node_core_logger');

//db_config에서 설정, 이는 현 디렉토리/config/db_config.json으로 설정
//이는 개인정보를 담고 있으므로 db_config.json은 gitignore를 통해 git에 track되면 안됨
var db_config  = require('./config/db_config.json');
var connection = mysql.createConnection({
    host     : db_config.host,
    user     : db_config.user,
    password : db_config.password,
    database : db_config.database,
});

//로그인 및 회원가입 구현 파트
class NodeLoginServer {
    constructor(config) {
        this.port = config.login.port 
        app.use(bodyParser.json());
        app.use(bodyParser.urlencoded({extended: true}));
        connection.connect();

        app.post('/user/login', function (req, res) {
            //여기서 sql 문의 경우 database에 어떤 스키마로 저장했는지에 따라 
            console.log(req.body);
            //body의 정보를 알아야 함, android에서 주는 이름과 일치해야 됨
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
            });
        });

	app.post('/user/join', function(req, res){
	    console.log(req.body);	    
/*	    var userEmail = req.body.userEmail;
	    var userName = req.body.userEmail;
	    connection.query(sql, userEmail, userName, function(err, result){

	    });
*/	    res.json({
		'code': 200,
		'message': 'test'
	    });
	});

    }

    run() {
        app.listen(this.port, () => {
            Logger.log(`Node Login Http Server started on port: ${this.port}`);
        });
    }
}

module.exports = NodeLoginServer;
