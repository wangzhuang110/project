import { fetchData } from '../services/Fetch';

export default {
    namespace: 'stateManege',
    state: {
        newAddrShow: false, //控制导航栏新建通信录点击时的显隐性
        tableHeight: null,  //详细信息显示区域的高度（滚动区域的高度）
        topMenuState: null,   //当前选中的菜单栏功能，为提供不同种类的右键菜单提供依据
        chanagIPVisible: false,//控制当树状图显示区显示的是全团编制时点击右键菜单“修改IP”表单的显隐性
        languageSelect: 'zh', //语言选择标志位
        addrGroup:[],       //通信录编组列表数据
    },

    effects: {
        // 控制导航栏新建通信录点击时的显隐性
        *changeState({ payload: data }, { call, put }) {
            yield put({
                type: 'ChangeState',
                payload: data
            })
        },
        *showDetail({ payload: data }, { call, put }) {
            // console.log('access showDetail')
            yield put({
                type: 'updataDetail',
                payload: data
            })
        },
        //记录当前菜单栏选中的功能
        *selectMenu({ payload: data }, { call, put }) {
            yield put({
                type: 'SelectMenu',
                payload: data.data1
            })
        },
        *changeIP({ payload: data }, { call, put }) {
            yield put({
                type: 'ChangeIP',
                payload: data
            })
        },
        *languageSelect({ payload: data }, { call, put }) {
            let language;
            if(data.data1 === true) {
                language = 'fr'
            }
            else {
                language = 'zh'
            }
            yield put({
                type: 'LanguageSelect',
                payload: language               //语言选择状态的切换调用了DispatchorLoacal文件里的函数，调用时只传来的data1，即是否选择了法语的状态值
            })
        },
        //请求通信录编组列表数据
        *getAddrGroup({ payload: dataset }, { call, put }) {
            const resData = yield call(fetchData,dataset);   
            const { resolve } = dataset;
            if(!resData.err) {
                if(resData.code === 1) {
                    if(resolve !== 'undefined')
                    !!resolve && resolve(resData); // 返回数据给有调用此方法且使用了Promise的组件
                    //将获取到的通信录编组数据保存
                        yield put({
                            type:'savaAddrGroup',
                            payload:resData.data
                        })
                }
            }
        }
    },

    reducers: {
        ChangeState(state, { payload: data }) {
            return {
                ...state,
                newAddrShow: data
            }
        },
        updataDetail(state, { payload: data }) {
            return {
                ...state,
                tableHeight: data
            }
        },
        SelectMenu(state, { payload: data }) {
            return {
                ...state,
                topMenuState: data
            }
        },
        ChangeIP(state, { payload: data }) {
            return {
                ...state,
                chanagIPVisible: data
            }
        },
        LanguageSelect(state, { payload: data }) {
            return {
                ...state,
                languageSelect: data
            }
        },
        savaAddrGroup(state, { payload: addrGroupData }) {
            return {
                ...state,
                addrGroup: addrGroupData
            }
        },
    }
}