// import {fetchData} from '../services/Fetch';
// import {getTreeData} from '../services/Fetch';
// export default {
//     namespace:'drawer',
//     state:{
//         drawerVisible:false,
//         addrList:[]

//     },

//     effects:{
//         //更改通信录列表的显隐性
//         *saveDrawerVisible({payload:data},{call,put}){
//             yield put({
//                 type:'SaveDrawerVisible',
//                 payload:data
//             })
//         },
//         //得到并保存通信录列表数据
//         *saveAddrList({payload:url},{call,put}){
//             // console.log('进入请求通信录列表数据')
//             let fetchPra = {url:url,method:'get'};
//             const data = yield call(fetchData,fetchPra);
//             // console.log('data',data )
            
//             yield put({
//                 type:'SavaAddrList',
//                 payload:data
//             })
//         },
//         //删除通信录列表里的文件
//         *deleteAddrList({payload:dataset},{call,put,select }){
//             // console.log('进入到请求删除跳转流程···');
//             const data = yield call(fetchData,dataset);
//             console.log('data',data)
//             //如果删除成功，在本地的addrList直接删除对应的文件列表
//             if(data.code === 200){
//                 yield put({
//                     type:'SavaAddrList',
//                     payload:data.data
//                 })
//                 console.log('完成删除后通信录列表的更新')
//             }
//         },
//         *rename({payload:dataset},{call,put }){
//             const {resolve} = dataset
//             const data = yield call(getTreeData,dataset.payload);
//             //如果删除成功，在本地的addrList直接删除对应的文件列表
//             if(data.data.code === 200){
//                 !!resolve && resolve(data); // 返回数据
//                 yield put({
//                     type:'SavaAddrList',
//                     payload:data.data.data
//                 })
                
//             }
//         },
//     },

//     reducers:{
//         SaveDrawerVisible(state,{payload:data}){
//             return {
//                 ...state,
//                 drawerVisible:data
//             }
//         },
//         SavaAddrList(state,{payload:data}){
//             return {
//                 ...state,
//                 addrList:data
//             }
//         },
        
//     }

// }