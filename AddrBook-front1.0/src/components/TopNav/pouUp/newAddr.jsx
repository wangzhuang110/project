import React from 'react';
import { connect } from 'dva';
import { Modal,Radio } from 'antd';
import DispatchForLocal from '../../../utils/DispatchForLoacal';
import Dispatch1 from '../../../utils/Dispatch1';
import { URL } from '../../../utils/url';
const RadioGroup = Radio.Group;

@connect(({ treedata, stateManege }) => ({
    treedata,stateManege
}))
export default class NewAddr extends React.Component {

    state = {
        visible:false,
        // addrGroup : ['aaa','bbb','ccc','ddd','eee','fff','ggg','fff','iii','jjj','kkk'],
        nowValue:null       //当前选中的编组的在数组中的下标值
    }
    
    handleOk = () => {
        const {dispatch} = this.props;
        // console.log(this.state.nowValue);
        dispatch({
            type:'stateManege/changeState',
            payload:false
        })
        if(this.state.nowValue === null) {
            return null;
        }
        const { addrGroup } = this.props.stateManege;
        // Dispatch1 = (dispatch,type,outResolve,url,method)
        if(addrGroup.length !==0) {
            const type = 'treedata/getTreeData';
            const url = `http://${URL.prefix}/group/get?name=${addrGroup[this.state.nowValue]}`;
            new Promise((resolve) =>{
                Dispatch1(dispatch,type,resolve,url,'get')
            }).then((res) => {
                if(res.data.code ===1) {
                    const type1 = 'treedata/checkedKeys';  
                    const type2 = 'treedata/saveCheckInfo';
                    const checkedKeys = [];
                    //清空已保存的节点的key
                    DispatchForLocal(dispatch,type1,null,checkedKeys,null);   
                    //清空详细信息显示区域已保存（显示的）信息
                    DispatchForLocal(dispatch,type2,null,[],null);   
                }
            })
            
        }
    }

    handleCancel = (e) => {
        const {dispatch} = this.props;
        dispatch({
            type:'stateManege/changeState',
            payload:false
        })
    }
    showAddrGroup = (data) => {
        const radioStyle = {
            display: 'block',
            height: '30px',
            lineHeight: '30px',
          };
        return data.map((item,index) => {
            return <Radio key={index} style={radioStyle} value={index}>{item}</Radio>
        })
    }

    onChange = (e) => {
        // console.log(e.target.value)
        this.setState({
            nowValue:e.target.value
        })
    }
    render(){
        const {newAddrShow,addrGroup} = this.props.stateManege;
        return (
            <Modal
                title="新建通信录 -- 选择通信录编组"
                visible={newAddrShow}
                onOk={this.handleOk}
                onCancel={this.handleCancel}
                okText = '确定'
                cancelText = '取消'
            >
                <RadioGroup onChange = {this.onChange}>
                    {this.showAddrGroup(addrGroup)}
                </RadioGroup>
            </Modal>
        )
    }
}