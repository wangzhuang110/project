import React from 'react';
import { connect } from 'dva';
import { Input, Menu } from 'antd';
import DisPatchForLocal from '../utils/DispatchForLoacal';
import './TreeShow/tree.less'
const Search = Input.Search;
let ShowSearchList2;

@connect(({ treedata }) => ({
  treedata
}))

export default class SearchBox extends React.Component {

  constructor(props) {
    super(props)
    this.state = {
      checkedKeys: [],
      showSearchList: false,
      defaultValue: '',
      SearchResult: []
    }
  }

  onSearch = (data) => {
    const searchData = data.split("(");
    console.log(searchData[0],data)
    const treeData = this.props.data;
    let resData = [];   
    let searchKey = [];                           //用于存放找到的数据
    this.filterArray(treeData, searchData[0], resData, searchKey);
    if (resData.length !== 0) {
      ShowSearchList2 = document.getElementById("ShowSearchList2");
      const treeShowArea = document.getElementById("treeShow");
      ShowSearchList2.style.display = "none";
      treeShowArea.style.display = 'block';
      // dispatch,type,resolve,data1,data2
      const{ dispatch } = this.props;
      const type1 = 'treedata/searchResult';
      const type2 = 'treedata/expandedKeys';
      DisPatchForLocal(dispatch, type1, null, searchData[0], null)
      DisPatchForLocal(dispatch, type2, null, searchKey, null);
    }
  }

  filterArray = (data, searchStr, searchArr, searchKey) => {
    //  console.log('data',data)
     let treeData = [];
     if(Array.isArray(data) === true) {
      treeData = data
    }
    else {
      treeData.push(data);
    }
    treeData.forEach((item) => {
      if ((item !== 'undefined') &&(item.name.indexOf(searchStr) !== -1) && (searchStr !== '')) {
        const { groupId, ip, name, type, unitId, unitSortCode, id } = item;
        let newItem = [];
        newItem = { groupId, ip, name, type, unitId, unitSortCode, id };  //只显示搜索到的节点， 不显示其下级节点
        searchArr.push(newItem);
        searchKey.push(item.id)
      }
      if (item.children && item.length !== 0) {
        this.filterArray(item.children, searchStr, searchArr, searchKey);
      }
      if (item.seat) {
        this.filterArray(item.seat, searchStr, searchArr, searchKey);
      }
      if (item.equipment) {
        this.filterArray(item.equipment, searchStr, searchArr, searchKey);
      }
    })

  }

  onChange = (e) => {
    ShowSearchList2 = document.getElementById("ShowSearchList2");
    const treeShowArea = document.getElementById("treeShow");
    const { dispatch } = this.props;
    
    // treeShow = document.getElementById("treeShow");
    if (e.target.value === '') {
      ShowSearchList2.style.display = "none";
      treeShowArea.style.display = 'block';
      //将搜索后的保存的搜索的字符的数据清空，以便高亮显示处变回原始的颜色
      const type = 'treedata/searchResult';
      DisPatchForLocal(dispatch, type, null, [], null)
    }
    else {
      treeShowArea.style.display = 'none';
      ShowSearchList2.style.display = "block";
    }

    let searchArr = [];
    let searchKey = [];
    const searchStr = e.target.value; //e.target.value为String类型
    const treeData  = this.props.data;
    // console.log('treeData',treeData);
    if (treeData.lenght !== 0 && searchStr !== '') {
      this.filterArray(treeData, searchStr, searchArr, searchKey);
      const { dispatch } = this.props;
      dispatch({
        type: 'treedata/savaSearchData',
        payload: searchArr
      })
    }

  }

  handleClickMenu = (data) => {
    ShowSearchList2 = document.getElementById("ShowSearchList2");
    const treeShowArea = document.getElementById("treeShow");
    // console.log('onclick', data.key)
    const { children } = data.item.props;
    if (children === '无搜索结果...') {
      return
    }
    const name = children[0];
    const { key } = data
    const clickData = `${name}(${key})`;
    // const searchVal = document.getElementById('search').value;

    document.getElementById('search2').value = clickData;
    ShowSearchList2.style.display = "none";
    treeShowArea.style.display = "block";
  }

  FulloFMenu = () => {

    const { SearchData } = this.props.treedata;
    
    // console.log('SearchData',SearchData)

    let x = null;
    if (SearchData.length !== 0) {

      x = SearchData.map(item => {
        return (<Menu.Item key={item.id}>{item.name} {item.ip} </Menu.Item>);
      })
    }
    else {
      x = <Menu.Item>无搜索结果...</Menu.Item>;
    };
    return x;
  }

  render() {
    return (
      <div>
        <Search
          placeholder="输入名称或IP"
          enterButton
          size="large"
          onSearch={this.onSearch}
          onChange={this.onChange}
          defaultValue={this.state.defaultValue}
          id="search2"
        />
        <div id='ShowSearchList2'>
          <Menu theme="dark" onClick={this.handleClickMenu}>
            {this.FulloFMenu()}
          </Menu>
        </div>
      </div>

    )
  }
}