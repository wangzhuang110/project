import React from 'react';
import { Switch } from 'antd';
import {connect} from 'dva';
import dispatchForLocal from '../../../utils/DispatchForLoacal';
import exit from '../../../assets/nav/退出.png';
import './topNavigation.less';

@connect(({ stateManege }) => ({
    stateManege
}))

export default class TopNavigation extends React.Component {

    languageSelect = (e) => {
        const { dispatch } = this.props;
        // Dispatch2 = (dispatch,type,resolve,data1,data2)
        dispatchForLocal(dispatch,'stateManege/languageSelect',null,e)
    }

    render() {
        return (
            <div className='topNav1'>
                <div className='topNavigation'>
                    <span className='title'>通信录服务软件</span>
                </div>
                <div className='icon1'>
                    <div className='language'>
                        <Switch checkedChildren="Français" unCheckedChildren="Français" onClick={this.languageSelect} />
                    </div>
                    <div className='topExit'><img src={exit} alt="asf" /></div>
                </div>  
               
            </div>
        )

    }

} 