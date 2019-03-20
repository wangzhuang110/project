import React from 'react';
import { connect } from 'dva';
import { Icon, Upload, message } from 'antd';
import DispatchForLocal from '../../../utils/DispatchForLoacal';
import request from '../../../utils/request';
import { URL } from '../../../utils/url';
import { TopMenuNum } from '../../../utils/TopMenuNum'
import Dispatch1 from '../../../utils/Dispatch1' ;
import newFile from '../../../assets/nav/new.png';
import open from '../../../assets/nav/打开.png';
import lock from '../../../assets/nav/lock.svg';
import unlock from '../../../assets/nav/unlock.svg';
import NewAddr from '../pouUp/newAddr';
import { FormattedMessage } from 'react-intl';
import { fetchData } from '../../../services/Fetch';
import './topMenu.less';

const type1 = 'stateManege/selectMenu'; 
const type2 = 'treedata/checkedKeys';  
const type3 = 'treedata/saveCheckInfo';

@connect(({ treedata, stateManege }) => ({
    treedata, stateManege
}))

export default class TopMenu extends React.PureComponent {

    state = {
        current: 'mail',
        stretch: 'flex',
        shrink: 'none',   //菜单收缩状态
        lock: lock,       //导航栏锁（图标）
        recordClick: [],   //记录当前点击的菜单栏功能的编号，从左数，数字1为初始值，+1递增;
    }

    changeRecordClick = (num) => {
        if (num === this.state.recordClick) {
            return null;
        }
        const a = document.getElementById(`${num}`).style;
        a.background = '#141f3e';
        if (this.state.recordClick.length !== 0) {
            const b = document.getElementById(`${this.state.recordClick}`);
            // b.background='none'
            b.removeAttribute('style')
        }
        this.setState({ recordClick: num })

    }
    //新建通信录
    newAddrList = (e) => {
        this.changeRecordClick(TopMenuNum.newAddr);
        const type = 'stateManege/selectMenu';
        const { dispatch } = this.props;

        new Promise((resolve) => {
            const {dispatch} = this.props;
            const type = 'stateManege/getAddrGroup'
            // Dispatch = (dispatch,type,outResolve,url,methond) 
            Dispatch1(dispatch,type,resolve,URL.getAddrGroup,'get');
        }).then((res) => {
            //获取通信录编组数据成功
            if(!res.err) {
               
                if(res.code === 1) {
                    //设置弹出选择通信录编组框
                    dispatch({
                        type: 'stateManege/changeState',
                        payload: true,
                    }).then(
                        //改变当前菜单栏选中的功能
                        DispatchForLocal(dispatch,type,null,TopMenuNum.newAddr,null)  
                    )
                }
            }
           
        })
       
    }
    openServerAddr = () => {
        this.changeRecordClick(TopMenuNum.openServerAddr);
        const { dispatch } = this.props;
        const getData = 'treedata/getTreeData';
        const clearKeys = 'stateManege/selectMenu';
        // Dispatch1 = (dispatch,type,outResolve,url,method)
        //获取服务器上存放的通信录数据
        new Promise(resolve => {
            Dispatch1(dispatch, getData,resolve,URL.getExistAddr,'get')
        }).then(res => {
            if(res.data.code === 5001) {
                message.error('数据未找到')
            }
        })
       
        //都是显示通信录，所以设置此时选中的菜单功能为“新建通信录”,对应标志为 1。
        // 参数(dispatch,type,resolve,data1,data2)
        DispatchForLocal(dispatch,clearKeys,null,TopMenuNum.openServerAddr,null);
         //清空已保存的节点的key
        DispatchForLocal(dispatch,type2,null,[],null);   
        //清空详细信息显示区域已保存（显示的）信息
        DispatchForLocal(dispatch,type3,null,[],null);   
    }
    //打开通信录
    openAddr = () => {
        
        const { dispatch } = this.props;
         //修改当前选中的菜单功能
         DispatchForLocal(dispatch,type1,null,TopMenuNum.openLocalAddr,null);
         //清空已保存的节点的key
         DispatchForLocal(dispatch,type2,null,[],null);   
         //清空详细信息显示区域已保存（显示的）信息
         DispatchForLocal(dispatch,type3,null,[],null);   
    }
    //打开通信录并将其上传给后天解析时，上传状态的回调函数
    onChange = (info) => {
        if (info.file.status === 'done') {
          message.success(`${info.file.name} 上传后台解析成功`);
        } else if (info.file.status === 'error') {
          message.error(`${info.file.name} file upload failed.`);
        }
        const resData = info.file.response;
        if(resData !== undefined) {
            if(resData.code === 1) {
                const { dispatch } = this.props;
                const addrListData = resData.data;
                const type = 'treedata/updataTreeData';
                DispatchForLocal(dispatch, type, null, addrListData, null);
                this.changeRecordClick(TopMenuNum.openLocalAddr);
            }
        }
        
    }

    //导出通信录
    export = () => {
        this.changeRecordClick(TopMenuNum.exportAddr);
        request(URL.exportAddrList);
    }
    //点击初始化全团编制的回调函数
    //获取编制信息并显示在界面的树状图显示区域
    initEquipmentInfo = () => {
        this.changeRecordClick(TopMenuNum.initEquipment)
        const { dispatch } = this.props;
        const type = 'treedata/getTreeData';
        new Promise((resolve) => {
            Dispatch1(dispatch, type, resolve, URL.initEquipment, 'get');
            }).then((res) => {
                if(res.data.code === 1) {
                    //设定当前菜单栏选中的功能
                    // Dispatch2 = (dispatch,type,resolve,data1,data2)
                    const type1 = 'stateManege/selectMenu'; 
                    const type2 = 'treedata/checkedKeys';  
                    const type3 = 'treedata/saveCheckInfo';
                    //修改当前选中的菜单功能
                    DispatchForLocal(dispatch,type1,null,TopMenuNum.initEquipment,null);
                    //清空已保存的节点的key
                    DispatchForLocal(dispatch,type2,null,[],null);   
                    //清空详细信息显示区域已保存（显示的）信息
                    DispatchForLocal(dispatch,type3,null,[],null);   
                }
        })
    }
    //点击全团编制的回调函数
    equipmentInfo = () => {
        this.changeRecordClick(TopMenuNum.equipment);
        const { dispatch } = this.props;
        const type = 'treedata/getTreeData';
        // Dispatch = (dispatch,type,resolve,url,method)
        new Promise((resolve) => {
            Dispatch1(dispatch, type, resolve, URL.getEquipment, 'get');
        }).then((res) =>{
            if(res.data.code === 5001) {
                message.error('数据未找到')
            }
            if(res.data.code === 1) {
                //设定当前菜单栏选中的功能
                const type1 = 'stateManege/selectMenu';
                const type2 = 'treedata/checkedKeys';  
                const type3 = 'treedata/saveCheckInfo';
                 //修改当前选中的菜单功能
                DispatchForLocal(dispatch,type1,null,TopMenuNum.equipment,null);
                //清空已保存的节点的key
                DispatchForLocal(dispatch,type2,null,[],null);   
                //清空详细信息显示区域已保存（显示的）信息
                DispatchForLocal(dispatch,type3,null,[],null);   
            }
        })
    }

    help = () => {
        this.changeRecordClick(TopMenuNum.help);
    }
    //导航栏展开时点击收缩开关的回调函数
    handeFlex = () => {
        if (this.state.lock === unlock) {
            return null;
        }
        this.setState({
            stretch: 'none',
            shrink: 'flex'
        }, () => {
            const tableHeight = document.getElementById('showinfo').offsetHeight - 117;
            const element = document.getElementById('treeshow');    //树状图显示区域元素
            const element2 = document.getElementById('treeShow');   //显示树形图区域的元素
            const treeHeight = element.offsetHeight-40;             //树状图显示区域元素的高度-搜索框的高度(40)=显示树形图区域的高度    
            element2.style.height = `${treeHeight}px`;
            const { dispatch } = this.props;
            dispatch({
                type: 'stateManege/showDetail',
                payload: tableHeight
            })
        })
    }
    //导航栏收缩时点击收缩开关的回调函数
    handeShrink = () => {
        const tableHeight = document.getElementById('showinfo').offsetHeight - 117 - 88;
        const element = document.getElementById('treeshow');    //树状图显示区域元素
        const element2 = document.getElementById('treeShow');   //显示树形图区域的元素
        const treeHeight = element.offsetHeight-40-88;             //树状图显示区域元素的高度-搜索框的高度(40)-导航栏扩大的高度(88)=显示树形图区域的高度
        element2.style.height = `${treeHeight}px`;
        const { dispatch } = this.props;
        dispatch({
            type: 'stateManege/showDetail',
            payload: tableHeight
        })
        this.setState({
            stretch: 'flex',
            shrink: 'none'
        })
    }
    //鼠标进入菜单栏时的回调函数
    //功能：鼠标进入菜单栏，伸展菜单栏
    handelMouseOver = () => {
        if (this.state.lock === lock) {
            return null;
        }
        
        const tableHeight = document.getElementById('showinfo').offsetHeight - 117 - 88;
        const element = document.getElementById('treeshow');    //树状图显示区域元素
        const element2 = document.getElementById('treeShow');   //显示树形图区域的元素
        const treeHeight = element.offsetHeight-40-88;          //树状图显示区域元素的高度-搜索框的高度(40)-导航栏扩大的高度(88)=显示树形图区域的高度
        element2.style.height = `${treeHeight}px`;
        const { dispatch } = this.props;
        dispatch({
            type: 'stateManege/showDetail',
            payload: tableHeight
        })

        this.setState({
            stretch: 'flex',
            shrink: 'none'
        })
    }
    //鼠标离开菜单栏时的回调函数
    //功能：鼠标拉开菜单栏，收缩菜单栏
    handelMouseOut = () => {
        if (this.state.lock === lock) {
            return null;
        }
        this.setState({
            stretch: 'none',
            shrink: 'flex'
        }, () => {
            const tableHeight = document.getElementById('showinfo').offsetHeight - 117;
            const element = document.getElementById('treeshow');    //树状图显示区域元素
            const element2 = document.getElementById('treeShow');   //显示树形图区域的元素
            const treeHeight = element.offsetHeight-40;             //树状图显示区域元素的高度-搜索框的高度(40)=显示树形图区域的高度
            element2.style.height = `${treeHeight}px`;
            const { dispatch } = this.props;
            dispatch({
                type: 'stateManege/showDetail',
                payload: tableHeight
            })
        })
    }
    //点击导航栏锁的按钮，变换锁的图标（解锁/锁）
    changeIcon = () => {
        const icon = this.state.lock;
        if (icon === lock) {
            this.setState({
                lock: unlock
            })
        }
        else if (icon === unlock) {
            this.setState({
                lock: lock
            })
        }
    }
    //更新提醒回调函数
    updata = () => {
        this.changeRecordClick(TopMenuNum.updata);
        const dataset = {'url':URL.updata,'method':'get'};
        fetchData(dataset).then(res => {
            if(res.code === 1) {
                message.success('更新提醒成功')
            }
            else {
                message.success('更新提醒失败')
            }
        })

    }

    render() {

        return (
            <div className='topMenu'>
                <NewAddr />
                {/* 展开状态下的菜单来 */}
                <div
                    style={{ display: this.state.stretch, flexDirection: "row" }}
                    onMouseLeave={this.handelMouseOut}
                >
                    <div className='part1'>
                        <div className="top">
                            <div className='menu'>
                                {/* 新建通信录 */}
                                <div id={TopMenuNum.newAddr} className='menuBlock'>
                                    <div className='link' onClick={this.newAddrList}>
                                        <img src={newFile} alt="asf" />
                                        <div>
                                            
                                            <FormattedMessage id='topMenu_newAddr' defaultMessage='Hello, Howard!' />  
                                        </div>
                                    </div>
                                </div>
                                {/* 通信录 */} 
                                <div id={TopMenuNum.openServerAddr} className='menuBlock'>
                                    <div className='link' onClick={this.openServerAddr}>
                                        <img src={newFile} alt="asf" />
                                        <div>
                                            <FormattedMessage id='topMenu_openServerAddr' defaultMessage='Hello, Howard!' />  
                                        </div>
                                    </div>
                                </div>
                                {/* 打开通信录 */}
                                <Upload 
                                    name='file'
                                    action={URL.openAddrList}
                                    onChange={this.onChange}
                                    showUploadList={false}
                                    //  headers={this.contentType}
                                >
                                    <div id={TopMenuNum.openLocalAddr} className='menuBlock'>
                                        <div className='link' onClick={this.openAddr}>
                                            <img src={open} alt="asf" />
                                            <div>
                                                <FormattedMessage id='topMenu_openAddr' defaultMessage='Hello, Howard!' />
                                            </div>
                                        </div>
                                    </div>
                                </Upload>
                                {/* 导出通信录 */}
                                <div id={TopMenuNum.exportAddr} className='menuBlock'>
                                    <a href={URL.exportAddrList} download className='link'>
                                        <img src={open} alt="asf" />
                                        <div>
                                            <FormattedMessage id='topMenu_export' defaultMessage='Hello, Howard!' />
                                        </div>
                                    </a>
                                </div>
                            </div>
                            <div style={{ color: "#909CB9", fontSize: "12px" }}><FormattedMessage id='word_file' /></div>
                        </div>
                        <div className="divider"></div>
                        <div className="top">
                            <div className="menu">
                                <div id={TopMenuNum.initEquipment} className='menuBlock'>
                                    <div className='link' onClick={this.initEquipmentInfo}>
                                        <img src={open} alt="asf" />
                                        <div>初始化编制</div>
                                    </div>
                                </div>
                                <div id={TopMenuNum.equipment} className='menuBlock'>
                                    <div className='link' onClick={this.equipmentInfo}>
                                        <img src={open} alt="asf" />
                                        <div>全团编制</div>
                                    </div>
                                </div>
                            </div>
                            <div style={{ color: "#909CB9", fontSize: "12px" }}>全团编制</div>
                        </div>
                        <div className="divider"></div>
                        <div className="top">
                            <div className="menu">
                                <div id={TopMenuNum.updata} className='menuBlock'>
                                    <div className='link' onClick={this.updata}>
                                        <img src={open} alt="asf" />
                                        <div>更新提醒</div>
                                    </div>
                                </div>
                            </div>
                            <div style={{ color: "#909CB9", fontSize: "12px" }}><FormattedMessage id='word_help' /></div>
                        </div>
                        <div className="divider"></div>
                        <div className="top">
                            <div className="menu">
                                <div id={TopMenuNum.help} className='menuBlock'>
                                    <div className='link' onClick={this.help}>
                                        <img src={open} alt="asf" />
                                        <div><FormattedMessage id='word_help' /></div>
                                    </div>
                                </div>
                            </div>
                            <div style={{ color: "#909CB9", fontSize: "12px" }}><FormattedMessage id='word_help' /></div>
                        </div>
                    </div>
                    <div className='part2'>
                        <div className='icon'>
                            <Icon type="up" style={{ fontSize: '16px', color: '#bbc4da' }} onClick={this.handeFlex} className='contract' />
                            <img src={this.state.lock} alt="asf" onClick={this.changeIcon} className='lock' />
                        </div>
                    </div>
                </div>

                {/* 收缩状态下的菜单栏 */}
                <div
                    className='shrink'
                    style={{ display: this.state.shrink}}
                    onClick={this.onClick}
                    onMouseEnter={this.handelMouseOver}
                >           
                    <div className='tool'>
                        <span>工具栏</span>
                    </div>    
                    
                    <div className='part2'>
                        <Icon type="down" style={{ fontSize: '16px', color: '#bbc4da' }} onClick={this.handeShrink} className='contract' />
                        <img src={this.state.lock} alt="asf" onClick={this.changeIcon} className='lock' />
                    </div>
                </div>


            </div>
        )
    }
}