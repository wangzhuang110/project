const  Dispatch = (dispatch,type,outResolve,url,method) => {

return  new Promise((resolve)=>{
        dispatch({
        type:type,
        payload:{   
            resolve,
            url,
            method,
        }
        })
    }).then((res)=>{
        // if(res.code === 1 | res.data.code === 1){
             if(outResolve !== null)
            !!outResolve && outResolve(res); // 返回数据给有调用此方法且使用了Promise的组件
         
    }) 
}
export default Dispatch;