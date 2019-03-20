const  Dispatch = (dispatch,type,url,methond) => {

return  new Promise((resolve)=>{
        dispatch({
        type:type,
        payload:{   
            resolve,
            url,
            methond,
        }
        })
    }).then((res)=>{
        if(res.data.code === 200){
            
        }
    }) 
}
export default Dispatch;