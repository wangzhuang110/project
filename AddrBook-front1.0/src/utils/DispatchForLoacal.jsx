const  Dispatch2 = (dispatch,type,resolve,data1,data2) => {
        dispatch({
        type:type,
        payload:{   
            resolve,
            data1,
            data2
        }
        })
   
}
export default Dispatch2;