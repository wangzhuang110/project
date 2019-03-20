import React from 'react';
import { connect } from 'dva';
import { Tree, Radio, message } from 'antd';
import SearchBox from '../Search';
import ChangeIP from '../contextmenu/changeIP';     //修改编制IP弹窗
import DispatchForLocal from '../../utils/DispatchForLoacal';
import Dispatch1 from '../../utils/Dispatch1';
import RightClickMenu from '../../utils/rightClickMenu';
import rightClickPosition from '../../utils/rightClickPosition';
import { URL } from '../../utils/url';
import Worker from './tree.webworker';
import './tree.less'
const config = window.config;
const TreeNode = Tree.TreeNode;
const RadioGroup = Radio.Group;
let worker;
@connect(({ treedata,stateManege }) => ({
  treedata,stateManege
}))
export default class TreeShow extends React.PureComponent {

  state = {
    expandedKeys: ['01团'],
    autoExpandParent: true,
    checkedKeys: [],
    selectedKeys: [],
    showSearchList: false,
    SearchResult: [],
    sendData:[],
    checkInfo : [],           //树状图点击后勾选的数据   
    defaultExpandAll:false,    
    checkStrictly:true,     
  }
  ShowSearchList = document.getElementById("ShowSearchList");
  treeShow = document.getElementById("treeShow");
  componentDidMount = () => {
    const {dispatch} = this.props;
    const type1 = 'treedata/getTreeData';
    const type2 = 'stateManege/selectMenu';

    const element = document.getElementById('treeshow');
    const tableHeight = element.offsetHeight-40;
    const element2 = document.getElementById('treeShow');
    element2.style.height = `${tableHeight}px`;
    // Dispatch1 = (dispatch,type,outResolve,url,method)
    //软件启动，获取服务器中的已存在的通信录文件
    Dispatch1(dispatch, type1,null,URL.getExistAddr,'get')
    //设置当前选中的菜单栏功能，此时虽然没有点击“新建通信录”，但其效果是一样的，
    //都是显示通信录，所以设置此时选中的菜单功能为“新建通信录”,对应标志为 1。
    // 参数(dispatch,type,resolve,data1,data2)
    DispatchForLocal(dispatch,type2,null,1,null);
    worker = new Worker();
    // 监听子线程发来的消息
    worker.onmessage = (e) => {
      // console.log('recive message',e.data);
      if(typeof(e.data) === 'string') {
        return
      }
      message.info('通信录更新')
      const type3 = 'treedata/updataTreeData';
      DispatchForLocal(dispatch,type3,null,e.data,null);
    }

    // 主线程向Worker发消息
    worker.postMessage({
      type:'init',
      ip:config.ip,
      port: config.webSocketPort,
      host:URL.processPrefix,
    });

    document.onclick = function () {
      let menu1 = document.getElementById("menu");
      if (menu1 !== null) {
        menu1.style.display = "none";
      }
      }
  }

  onExpand = (expandedKeys) => {
    const { dispatch } = this.props;
    // console.log('expandedKeys',expandedKeys)
    this.setState({
      expandedKeys,
      autoExpandParent: false,
    });
    const type = 'treedata/expandedKeys';
    DispatchForLocal(dispatch, type, null, expandedKeys, null);
  }

  onfilterTreeNode = (treeNode) => {
    
    const { searchResult } = this.props.treedata;
    if(searchResult.length === 0) {
      return null
    } 
    const node = treeNode.props.dataRef;
    if(node.name.indexOf(searchResult) !== -1) {
      return true
    }
  }

  changeCheckNode = (checkNodes) => {
    let newCheckNodes = [];
    checkNodes.forEach((item)=>{
      let node = {};
      node.key         = item.key;
      node.name        = item.props.title;
      node.type        = item.props.dataRef.type;
      node.ip          = item.props.dataRef.ip;
      // node.emailAddr   = item.props.dataRef.ip;
      newCheckNodes.push(node)
    })
      return newCheckNodes;
    }
    
  onCheck = (checkedKeys,checkInfo) => {
    const {dispatch} = this.props;
    // (dispatch,type,resolve,data1,data2
    const checkNodes = this.changeCheckNode(checkInfo.checkedNodes);
    const type1 = 'treedata/saveCheckInfo';
      const type2 = 'treedata/checkedKeys';
      DispatchForLocal(dispatch,type1,null,checkNodes,null);   //保存勾选的节点的数据
      DispatchForLocal(dispatch,type2,null,checkedKeys,null);   //保存勾选的节点的key
      this.setState({ checkedKeys });
  }

  selectKey = (selectKey) => {
    const node = {
      'key'  : selectKey.id,
      'name' : selectKey.name,
      'type' : selectKey.type,
      'ip'   : selectKey.ip,
    }
    return node;
  }

  onSelect = (selectedKeys, info) => {
    const selectedKey = info.node.props.dataRef;
    const newSeletedKey = this.selectKey(selectedKey);
    console.log('newSeletedKey',newSeletedKey)
    this.setState({ selectedKeys });
    // DispatchForLocal(dispatch,type,null,newSeletedKey,null);   //保存勾选的节点的key
  }
  //给装备的席位增加id这个字段
  changeEquipSeats = (seats,item) => {
    const bossName = item.name; 
    const equipmentSn = item.equipmentSn;
    seats.forEach((item) => {
      item.id = `${bossName}${equipmentSn}${item.seatCode}`;
      item.bossEquipmentSn = equipmentSn;
      item.type = '装备席位'
    })
  } 
  changeEquipment = (data) => {
    let equipmentData = [];
    if(Array.isArray(data) === true) {
      equipmentData = data
    }
    else {
      equipmentData.push(data);
    }
    equipmentData.forEach((item) => {
      const bossName = item.name;
      item.id = `${item.name}${item.equipmentSn}`
      item.type = "装备"
      const seat = item.seats;
      this.changeEquipSeats(seat,item);
      
      const equipmentSn = item.equipmentSn;  //装备的equipmentSn编号
      const seats = {'name':`${bossName}席位`,"id":`${bossName}${equipmentSn}席位`,'type':`${bossName}席位列表节点`,'flag':'5',seat};
      delete(item.seats);
      // console.log('item',item)
      item.children=[];   //给equipment字段的对象添加chirdren字段（后台传来的数据没有此字段）
      item.children.unshift(seats)
    })
  }
  changeSeats = (seats,item) => {
    const bossName = item.name;
    const unitSortCode = item.unitSortCode;
    seats.forEach((item) => {
      item.id = `${bossName}${item.seatCode}`;
      item.bossUnitSortCode = unitSortCode;
      item.type = '席位'
    })
  }
  //将后台传来的通信录数据结构进行改变（seats放到children中）
  changeArr = (data) => {
  let treeData = [];
  if(Array.isArray(data) === true) {
    treeData = data
  }
  else {
    treeData.push(data);
  }
    treeData.forEach((item)=>{
      const bossName = item.name;
      const unitSortCode = item.unitSortCode; //上级的unitSortCode
      if(item.unitSortCode){
        if(item.unitSortCode.length === 2) {
          item.type = '团指挥所';
          item.id = `${unitSortCode}${bossName}`;
        }
        if(item.unitSortCode.length === 4) {
          item.type = '连指挥所';
          item.id = `${unitSortCode}${bossName}`;
        }
        if(item.unitSortCode.length === 6) {
          item.type = '营指挥所';
          item.id = `${unitSortCode}${bossName}`;
        }
      }
      if(item.equipmentSn) {
        item.type = '装备'
      }
      if(item.equipments) {
        // console.log('item.equipments',item.equipments.length);
        const equipment = item.equipments;
        
       
        this.changeEquipment(equipment)
        const equipments = {"name":`${bossName}装备列表`,'id':`${bossName}装备列表`,'type':`${bossName}装备列表节点`, "flag":"5",equipment}
        delete(item.equipments);
        item.children.unshift(equipments);  
        
      }
      if(item.seats){
        const seat = item.seats;
       
        this.changeSeats(seat,item);
        let seats = {"name":`${bossName}席位`,"id":`${bossName}${unitSortCode}席`,'type':'席位列表节点',"flag":"5",seat};
        delete(item.seats);
        item.children.unshift(seats);
      }
      
      if(item.children){
        
        this.changeArr(item.children)
      }
    })  
  }


  renderSeats = (data) =>{
    return  data.map((item)=>{
        return(
          <TreeNode title={item.name} key={item.id} dataRef={item} />
        )
      })
  }
  renderEquipment = (data) => {
    return data.map((item) => {
      if(item.children) {
        // console.log('item.name',item.name)
        return(
          <TreeNode title={item.name} key={item.id} dataRef={item}>
            {this.renderChild(item.children)}
          </TreeNode>
        )
        
      }
      // console.log('item.name',item.name)
      return <TreeNode title={item.name} key={item.id} dataRef={item}/>
    })
  }
  renderChild = (data)=>{
    return data.map((item)=>{
      if(item.seat){
        return(
          <TreeNode title={item.name} key={item.id} dataRef={item}>
             {this.renderSeats(item.seat)}
          </TreeNode>
        )
      }
      if(item.equipment){
        
        return(
          <TreeNode title={item.name} key={item.id} dataRef={item}>
             {this.renderEquipment(item.equipment)}
          </TreeNode>
        )
      }
      if(item.children){        
        return(
          <TreeNode title={item.name} key={item.id} dataRef={item}>
            {this.renderChild(item.children)}
          </TreeNode>
        )
      }
      return <TreeNode title={item.name} key={item.id} dataRef={item} />
    })
   
  }
  renderTreeNodes = (data)=>{
    if(data.length === 0){
      return 
    }

    let treeData = [];
    if(Array.isArray(data) === true) {
      treeData = data
    }
    else {
      treeData.push(data);
    }
     return treeData.map((item)=>{
        if(item.children){
          return(
            <TreeNode title={item.name} key={item.id} dataRef={item}>
              {this.renderChild(item.children)}
            </TreeNode>
          )
        }
        
        return <TreeNode title={item.name} key={item.id} dataRef={item} />;
      }
    )
  }

  onRightClick = (e) => {
    if(e.node.props.dataRef.flag) {
      return null
    }
    //获取包裹右键菜单的div的dom
    const menu = document.getElementById("menu");
    //控制右键菜单显示与位置  
    rightClickPosition(e,menu);
    //将右键的节点的信息保存，传递给右键菜单组件
    let sendData = [e.node.props];
    // console.log('e.node.props',e.node.props);
    //topMenuState不同表示右键菜单的不同
    const {topMenuState} = this.props.stateManege;
    // console.log('topMenuState',topMenuState)
    sendData.push(topMenuState);
    this.setState({
      sendData
    }) 
    const onRightClickNode = e.node.props.dataRef;
    const { dispatch } = this.props;
      dispatch({
        type: 'treedata/saveRightClickNode',
        payload: onRightClickNode
      })
  }
  changeSelect = (e) => {
    if(e.target.value === 'single') {
      this.setState({checkStrictly: true})
    }
    else if(e.target.value === 'double') {
      this.setState({checkStrictly: false})
    }
    
  }

  render() {
    const { treeData, expandedKeys} = this.props.treedata; 
    const data = JSON.parse(JSON.stringify(treeData));      //深拷贝
    this.changeArr(data);
    return (
      <div id="treeshow" className="treeshow">
        <SearchBox data={data}/>
        
        <div id="menu">
          <RightClickMenu msg={this.state.sendData}/> 
        </div>

        <div id='treeShow'>
         <RadioGroup defaultValue={'single'} onChange={this.changeSelect}> 
              <Radio value='single'>单选</Radio>
              <Radio value='double'>多选</Radio>
            </RadioGroup>
          <Tree
            checkable
            onExpand={this.onExpand}
            checkStrictly={this.state.checkStrictly}
            expandedKeys={expandedKeys}      //（受控）展开指定的树节点
            autoExpandParent={this.state.autoExpandParent}
            onCheck={this.onCheck}
            checkedKeys={this.state.checkedKeys}
            onSelect={this.onSelect}
            selectedKeys={this.state.selectedKeys}
            filterTreeNode={this.onfilterTreeNode}
            onRightClick={this.onRightClick}
          >
            {this.renderTreeNodes(data)}
          </Tree>
        </div>
          <ChangeIP />
      </div>

    )
  }
}