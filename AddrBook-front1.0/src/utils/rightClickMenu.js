import React from 'react';
import { Popconfirm,message } from 'antd';
import { connect } from 'dva';
import { TopMenuNum } from '../utils/TopMenuNum';
import { fetchData } from '../services/Fetch';
import { URL } from '../utils/url';
import './MouseRightClick.less';

@connect(({ treedata, stateManege }) => ({
    treedata, stateManege
}))

export default class RightClickMenu extends React.Component {


    addObj = () => {
        const { rightClickNode } = this.props.treedata;

        if (rightClickNode.type === '席位') {
            alert('暂时不可增加席位')
            return;
        }
    }


    cancel = (e) => {
        // const { rightClickNode } = this.props.treedata;
        // console.log('rightClickNode.type', rightClickNode.type, rightClickNode)
    }


    changeIP = () => {
        // const { msg } = this.props;
        // console.log('msg', msg);
        const { dispatch } = this.props;
        dispatch({
            type: 'stateManege/changeIP',
            payload: true,
        })

    }
    //下发通信录
    issueAddrList = () => {
        // request(URL.issue, 'post');
        
        const dataset = {'url':URL.issue,'method':'post'};
        // const dataset = {'url':'http://uop.ceiec.com/api/LK-0100004/LK004/issue','method':'post'};
        // uploadFormInfo('http://uop.ceiec.com/api/LK-0100004/LK004/issue','post')
        fetchData(dataset).then(res => {
            if(res.code === 1) {
                message.info('通信录下发成功');
            }
            else {
                message.info('通信录下发失败');
            }
        })
    }

    render() {
        const { msg } = this.props;
        let menu = [];
        switch (msg[1]) {
            //新建通信录或启动前端时走动接收通信录的情况
            case TopMenuNum.newAddr:
                menu = (
                    <div className='action' style={{ boxShadow: '0 4px 12px 3px rgba(0, 0, 0, 0.15)' }}>
                        <Popconfirm className='confirmTwo' title="是否要下发此通信录" onConfirm={this.issueAddrList} onCancel={this.cancel} okText="是" cancelText="否">
                            <div><span>下发</span></div>
                        </Popconfirm>
                    </div>
                );
                break;
            //点击菜单栏的“通信录”按钮出现的右键菜单
            case TopMenuNum.openServerAddr:
                menu = (
                    <div className='action' style={{ boxShadow: '0 4px 12px 3px rgba(0, 0, 0, 0.15)' }}>
                         <Popconfirm className='confirmTwo' title="是否要下发此通信录" onConfirm={this.issueAddrList} onCancel={this.cancel} okText="是" cancelText="否">
                            <div><span>下发</span></div>
                        </Popconfirm>
                    </div>
                ); break;
            case TopMenuNum.openLocalAddr:
                menu = (
                    <div className='action' style={{ boxShadow: '0 4px 12px 3px rgba(0, 0, 0, 0.15)' }}>
                        <Popconfirm className='confirmOne' title="是否要上传" onConfirm={this.upload} onCancel={this.cancel} okText="是" cancelText="否">
                            <div ><span>上传通信录</span></div>
                        </Popconfirm>
                    </div>
                ); break;
            //点击菜单栏“全团编制”或“初始化全团编制”时，在树状图显示区右键某节点出现的右键菜单
            case (TopMenuNum.initEquipment):
                menu = (
                    <div className='action' style={{ boxShadow: '0 4px 12px 3px rgba(0, 0, 0, 0.15)' }}>
                        <div onClick={this.changeIP}><span>修改IP</span></div>
                        {/* <div onClick={this.distribute}><span>下发</span></div> */}
                    </div>
                ); break;
            case (TopMenuNum.equipment):
                menu = (
                    <div className='action' style={{ boxShadow: '0 4px 12px 3px rgba(0, 0, 0, 0.15)' }}>
                        <div onClick={this.changeIP}><span>修改IP</span></div>
                        {/* <div onClick={this.distribute}><span>下发</span></div> */}
                    </div>
                ); break;
            default: break;
        }
        return menu

    }


}
