import React from 'react';
import { connect } from 'dva';
import { Form, Modal, Input } from 'antd';
import DispatchForLocal from '../../utils/DispatchForLoacal';    
import { URL } from '../../utils/url'; 
const FormItem = Form.Item;

const CollectionCreateForm = Form.create()(
    class extends React.Component {
        state = {
            IP: '5.5.5.5'
        }
        render() {
            const { visible, onCancel, onCreate, form } = this.props;
            const { getFieldDecorator } = form;
            let { msg } = this.props;
            return (

                <Modal
                    visible={visible}
                    title="修改IP"
                    okText="确定"
                    onCancel={onCancel}
                    onOk={onCreate}
                    cancelText="取消"
                >

                    <Form layout="vertical">
                        <FormItem label="IP">
                            {
                                getFieldDecorator('ip', {
                                    rules: [{
                                        required: true, message: '请正确输入IP地址，例如（192.168.1.1）',
                                        pattern: /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
                                    }],
                                    initialValue: msg.ip
                                })(<Input type="textarea" />)
                            }
                        </FormItem>
                    </Form>
                </Modal>
            );
        }
    }
);
@connect(({ stateManege, treedata }) => ({
    stateManege, treedata
}))
export default class ChangeIP extends React.Component {

    saveFormRef = (formRef) => {
        this.formRef = formRef;
        //console.log('formRef',this.formRef)
    }

    handleCancel = () => {
        this.setState({ visible: false });
        const { dispatch } = this.props;
        const state = false;
        dispatch({
            type: 'stateManege/changeIP',
            data: state
        })
    }
    //确定修改编制IP时，post方法的上传参数
    //上传参数:
    //code:所属上级节点的唯一标识
    //flag:1==>右键的节点时团、连、站的一种
    //     2==>右键的节点是装备
    //     3==>右键的节点是团/连/站的席位
    //     4==>右键的节点是装备的席位
    judgeFlag = (values) => {
        const { rightClickNode } = this.props.treedata;
        let param = { "code": null, "flag": null, "ip": null, "seatCode": null }
        //右键的节点时团、连、站的一种
        if (rightClickNode.unitSortCode) {
            param.code = rightClickNode.unitSortCode;
            param.flag = 1;
            param.ip = values.ip;
        }
        //右键的节点是装备
        else if (rightClickNode.equipmentSn) {
            param.code = rightClickNode.equipmentSn;
            param.flag = 2;
            param.ip = values.ip;
        }
        //右键的节点是团/连/站的席位
        else if (rightClickNode.bossUnitSortCode) {
            param.code = rightClickNode.bossUnitSortCode;
            param.flag = 3;
            param.ip = values.ip;
            param.seatCode = rightClickNode.seatCode;
        }
        //右键的节点是装备的席位
        else if (rightClickNode.bossEquipmentSn) {
            param.code = rightClickNode.bossEquipmentSn;
            param.flag = 4;
            param.ip = values.ip;
            param.seatCode = rightClickNode.seatCode;
        }

        return param;
    }
    handleCreate = () => {
        const { dispatch } = this.props;
        const form = this.formRef.props.form;
        form.validateFields((err, values) => {          //values是填写到表单的数据
            if (err) {
                return;
            }
            const param = this.judgeFlag(values);
            const url = `http://${URL.prefix}/ip/put`;
            fetch(url, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(param),

            }).then((res) => {
                return res.json(res)
            }).then((res) => {
                if (res.code === 1) {

                    const type = 'treedata/updataTreeData';
                    // DispatchForLocal参数：dispatch,type,resolve,data1,data2
                    DispatchForLocal(dispatch, type,null,res.data,null);
                }
            })
            form.resetFields();
            //点击确定后表单不显示    
            dispatch({
                type: 'stateManege/changeIP',
                data: false
            })
        });
    }

    render() {
        const { chanagIPVisible } = this.props.stateManege;
        const { rightClickNode } = this.props.treedata;
        if (rightClickNode.length === 0) {
            return null
        }
        // console.log('rightClickNode', rightClickNode)
        return (
            <div>
                {/* <Button type="primary" onClick={this.showModal}>New Collection</Button> */}
                <CollectionCreateForm
                    wrappedComponentRef={this.saveFormRef}
                    visible={chanagIPVisible}
                    onCancel={this.handleCancel}
                    onCreate={this.handleCreate}
                    msg={rightClickNode}
                />
            </div>
        );
    }

}