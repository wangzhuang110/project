import { getTreeData } from '../services/Fetch';
export default {
  namespace: 'treedata',
  state: {
    treeData: [],//树状图列表要显示的数据，默认情况下接收公共通信录数据。若是在通讯录列表点击了其他通信录数据，则填充其他通信录数据
    SearchData: [],  //存放模糊搜索得到的数据
    selected:[],       //存放点击选择搜索列表的数据
    rightClickNode:[],
    checkInfo:[],        //保存树状图选中的数据
    checkedKeys:[],      //勾选中的树的标识
    searchResult: [],    //存放搜索的的数据
    expandedKeys: ["01团"],
  },
  effects: {
    //默认情况下去请求公共通信录数据显示在树状图显示区
    *getTreeData({payload:dataset},{ call, put }) {
      const res_treedata = yield call(getTreeData,dataset.url);
      // console.log('res_treedata',res_treedata);
      if(!res_treedata.err) {
        const { resolve } = dataset; 
        !!resolve && resolve(res_treedata) // 返回数据
        if(res_treedata.data.code === 1) {
            yield put({
            type: 'FillTreeData',
            payload: res_treedata.data.data
          })
        }
      }
      
    },
    //更新树状视图区的显示数据（fetch_TreeData）
    *updataTreeData({payload},{ call, put }) {
        yield put({
          type: 'FillTreeData',
          payload: payload.data1
        })      
    },
    //在点击通信录列表某通信录的情况下使用此方法
    *clickList({payload:dataset},{call,put,select}){
      // console.log('dataset.payload',dataset.url)
      const data = yield call(getTreeData,dataset.url);
      // console.log('data',data)
      let a = [];
      a.push(data.data.data);
      // console.log('a',a,data.data.code)
      if(data.data.code === 200){
        yield put({
          type: 'FillTreeData',
          payload: data.data.data
        })
      }
    },
    *savaSearchData({payload:searchData},{call,put}){
        yield put({
          type:'SavaSearchData',
          payload:searchData
        })
    },
    *saveselected({payload:data},{call,put}){
        yield put({
          type:'saveSelected',
          payload:data
        })
    }, 
    *saveRightClickNode({payload:data},{call,put}){
      yield put({
        type:'saveNode',
        payload:data
      })
    },
    *saveCheckInfo({payload:data},{call,put}) {
      yield put({
        type:'SaveCheckInfo',
        payload:data.data1
      })
    },
    *addCheckInfo({payload:data},{call,put}) {
      console.log('addcheckedinfo',data.data1)
      yield put({
        type:'AddCheckInfo',
        payload:data.data1
      })
    },
    *checkedKeys({payload:data},{call,put}) {
      yield put({
        type:'saveCheckedKeys',
        payload:data.data1
      })
    },
    *searchResult({payload:data},{call,put}) {
      yield put({
        type:'saveSearchResult',
        payload:data.data1
      })
    },
    *expandedKeys({payload:data},{call,put}) {
      console.log('data',data)
      yield put({
        type:'saveExpandedKeys',
        payload:data.data1
      })
    },
  },

  reducers: {
    //将树状图数据重新写入
    FillTreeData(state, { payload: newTreeData }){
      return {
        ...state,
        treeData: newTreeData
      }
    },

    SavaSearchData(state,{payload:searchData}){
      return {
        ...state,
        SearchData:searchData
      }
    },
    saveSelected(state,{payload:data}){
      return {
        ...state,
        selected:data
      }
    },
    saveNode(state,{payload:data}){

      return {
        ...state,
        rightClickNode :data
      }
    },
    SaveCheckInfo(state,{payload:data}){
      return {
        ...state,
        checkInfo :data
      }
    },
    saveCheckedKeys(state,{payload:data}){
      return {
        ...state,
        checkedKeys :data
      }
    },
    saveSearchResult(state,{payload:data}){
      return {
        ...state,
        searchResult :data
      }
    },
    saveExpandedKeys(state,{payload:data}){
      return {
        ...state,
        expandedKeys :data
      }
    },
    AddCheckInfo(state,{payload:data}){
      const checkInfos =  state.checkInfo.push(data);
      return {
        ...state,
        checkInfos
      }
    },
  }
}

