// let config = window.config;

let config = window.config;
const host = config.host;
let prefix = null;
let processPrefix = null;
if(host.indexOf('localhost') !== -1) {
    prefix = `${config.ip}:${config.servicePort}`;
    processPrefix = `${config.ip}`
}
else {
    prefix = `${host}/api/LK-0100004/LK004`;
    processPrefix = `${host}`
}

let URL = {
    'prefix':`${prefix}`,
    'processPrefix':`${processPrefix}`,
    'getAddrData': `http://${prefix}/getPubAddrlist`,       //获取通信录数据
    'getAddrList': `http://${prefix}/getFileList`,
    'initEquipment': `http://${prefix}/unit/get`,          //初始化全团编制信息
    'getEquipment': `http://${prefix}/fileUnit/get`,        //获取全团编制信息
    'getAddrGroup': `http://${prefix}/name/get`,           //获取通信录编组列表
    'getExistAddr': `http://${prefix}/addr/get`,       //获取后端已存在的通信录文件并显示到树状图显示区
    'exportAddrList': `http://${prefix}/download`,         //导出（下载）通信录
    'openAddrList': `http://${prefix}/upload`,             //打开通信录（文件上传到后台解析的地址）
    'issue':`http://${prefix}/issue`,                       //通信录下发
    // 'issue':`http://${config.ip}:${config.servicePort}/issue`
    'updata':`http://${prefix}/addr/remind`
}
export { URL }