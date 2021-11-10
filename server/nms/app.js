const NodeMediaServer = require('node-media-server');

const config = {
  rtmp: {
    port: 1935,
    chunk_size: 60000,
    gop_cache: true,
    ping: 30,
    ping_timeout: 60
  },
  http: {
    port: 80,
    allow_origin: '*'
  },
  login:{
    port: 5000
  }

};


var nms = new NodeMediaServer(config)
nms.run();
