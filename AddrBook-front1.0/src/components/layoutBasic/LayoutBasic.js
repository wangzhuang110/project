import React, { Component } from 'react';
import './layoutBasic.less';
import { connect } from 'dva'
import TopMenu from '../TopNav/TopMenu/TopMenu';
import TopNavigation from '../TopNav/TopNavigation/TopNavigation'
import ShowInfo from '../ShowInfo/ShowInfo';
import TreeShow from '../TreeShow/TreeShow';
import ButtomState from '../ButtomState/ButtomState';
import { IntlProvider, addLocaleData } from 'react-intl';
//映入自定义的语言映射表
import zh_CH from '../../utils/language/zh_CH';
import fr_FR from '../../utils/language/fr_FR';
//引入中文、法文语言
import zh from 'react-intl/locale-data/zh';
import fr from 'react-intl/locale-data/fr';
//载入多个语言数据
addLocaleData([...zh, ...fr])

@connect(({ treedata, stateManege }) => ({
    treedata, stateManege
}))

export default class LayoutBasic extends Component {

    state = {
        menu: null
    }

    render() {
        let language = null;
        const { languageSelect } = this.props.stateManege;
        if(languageSelect === 'zh') {
            language = zh_CH
        }
        else if(languageSelect === 'fr') {
            language = fr_FR
        }
        // console.log('language choice',language,languageSelect)
        return (
            <IntlProvider locale={languageSelect} messages={language}>
                <div className="layoutBasic">


                    <TopNavigation />
                    {/* <div className="topNav">     */}
                    <TopMenu />
                    {/* </div>  */}
                    <div className="content">
                        <div className="treeShow">
                            <TreeShow />
                        </div>
                        <div id="showinfo" className="showInfo">
                            <ShowInfo />
                        </div>
                    </div>
                    {/* <div> */}
                    <ButtomState />
                    {/* </div> */}
          

                </div>
            </IntlProvider>
        )
    }
} 