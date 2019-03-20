import io from 'socket.io-client';

let socket;

onmessage = function (e) {
  if(e.data.type === 'init') {
    // const host = e.data.host.split(":");
    const processPrefix = e.data.host;
    //连接后台web socket
    socket = io(`http://${processPrefix}:${e.data.port}`);
  }

  //连接成功
  socket.on('connect', function () {
    console.log('socket连接成功');
  }); 

  //连接失败
  socket.on('disconnect', function () {
    console.log('socket连接失败');
  }); 

  socket.on('msg',function(data){
    socket.emit('msg', {rp:"fine,thank you"}); //向服务器发送消息
    console.log('后台发来的消息',data);
    postMessage(data)
});
}

