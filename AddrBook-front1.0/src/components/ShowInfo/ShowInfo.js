import React, { PureComponent } from 'react';
import { Table } from 'antd';
import './showInfo.less'
import { connect } from 'dva';
// import { LocaleProvider } from 'antd';
// import fr_FR from 'antd/lib/locale-provider/fr_FR';
import moment from 'moment';
import 'moment/locale/zh-cn';

moment.locale('zh-cn');

const columns = [
    {
        title: '名称',
        dataIndex: 'name',
        width: '20%',
        className:'FontSize',
        align:'center',
        key:''
     
        //render: text => <a href="javascript:;">{text}</a>,
    },{
    title: '节点类型',
    dataIndex: 'type',
    width: '15%',
    className:'FontSize',
    align:'center',
    render: text => <div>{text}</div>,
},{
    title: 'IP地址',
    dataIndex: 'ip',
    width: '7%',
    className: 'FontSize',
    align:'center'
}, {
    title: '文电地址',
    dataIndex: 'emailAddr',
    width: '7%',
    className: 'FontSize',
    align:'center'
}
];
@connect(({stateManege,treedata}) =>({
    stateManege,treedata
}))
export default class ShowInfo extends PureComponent {

    state={
        tableHeight:null,
        data:[
            {key:"1"   , name: " ", type: " ", ip: " ", emailAddr: " "},
            {key:" 2"  , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 3"  , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 4"  , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 5"  , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 6"  , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 7"  , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 8"  , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 9"  , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 10" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 11" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 12" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 13" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 14" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 15" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 16" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 17" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 18" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 19" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 20" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 21" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 22" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 23" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 24" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 25" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 26" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 27" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 28" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 29" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 30" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 31" , name:" " , type: " ", ip: " ", emailAddr: " "},
            {key:" 32" , name:" " , type: " ", ip: " ", emailAddr: " "},
        ],
    }

    componentDidMount = () => {
        const tableHeight = document.getElementById('showinfo').offsetHeight-117;
        this.setState({tableHeight})
        const {dispatch} = this.props;
        dispatch({
            type:'stateManege/showDetail',
            payload:tableHeight
        })
       
    }
    changeCheckNode = (checkNodes) => {
        //checkNodes.length % 32 !== 0 说明checkNodes的长度不是32的倍数
        if(checkNodes.length % 32 !== 0) {
            const differenValue = checkNodes.length - 32;  //计算选中的点点的数量与32的差值
            //如果差值小于0，说明选中的点的数量小于32
            if(differenValue < 0) {
                const absDifferenValue = Math.abs(differenValue);
                for(let i = 0; i < absDifferenValue; i++) {
                    checkNodes.push({key:`${checkNodes.length+i}`, name: " ", type: " ", ip: " ", emailAddr: " "})
                }
            }
            //如果差值大于0，说明选中的点的数量大于32
            else {
                const length = checkNodes.length;
                const value = length/32;
                const  integerPart = Math.floor(value);     //value向下取正，eg: Math.floor(5.2) = 5
                const totalNum = 32+32*integerPart         //表格理论上需要显示的总行数
                const addValue = totalNum - length         //用理论总行数-现在显示的行数，得到还需要添加的行数
                for(let i = 0; i < addValue; i++) {
                    checkNodes.push({key:`${length+i}`, name: " ", type: " ", ip: " ", emailAddr: " "})
                }
            }
        }    
    }
    render() {
        const { tableHeight } = this.props.stateManege;
        if(tableHeight === null) {
            return null
        }
        const { checkInfo } = this.props.treedata;
        let checkNodes = []
        if(checkInfo.length === 0) {
            checkNodes = this.state.data
        }
        else {
            checkNodes = JSON.parse(JSON.stringify(checkInfo))
            this.changeCheckNode(checkNodes);
        }
        
        return (
           
            //  <div className="overflow" id='11'>
            // <LocaleProvider  locale={fr_FR}>
                <Table
                    scroll={{y:tableHeight}}
                    columns={columns}
                    // dataSource={this.state.data}
                    dataSource={checkNodes}
                    bordered
                    // title={()=><h1 className="Title">详细信息显示</h1>}
                    pagination={{ defaultPageSize:32}}
                    // footer={()=><h1 className="Title">详细信息显示</h1>}
                    
                >
                </Table>
            // </LocaleProvider>
            // </div>
             
        )
    }
}