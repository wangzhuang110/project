import request from '../utils/request';
export function query() {
  return request('/api/users');
}
//获取通信录具体信息
export function getTreeData(url) {
  return request(url);
}

//上传表单信息
export function uploadFormInfo(url,option) {
  return request(url,option);
}
//使用get方法的接口fetch时调用的函数
export function getData(url) {
  request(url);
  // if(data.)
}

export function fetchData(dataset) {
  return fetch(dataset.url, {
    method: dataset.method
  }).then((res)=>{
    if(res.status>=200 && res.status<=300){
      return res;
    }
    const error = new Error(res.statusText);
    error.response = res;
    throw error;
  })
    .then((res) => {
       return res.json(res)
    }).then((res) => {
      if(res.code !== 1){
        alert("请求数据出错，出错原因:"+res.message)
      }
      
         return res;
    })
    .catch(err => ({ err }));

}