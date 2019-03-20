const rightClickPosition = (e,data)=> {
 
    e.event.preventDefault()
    //鼠标点的坐标
    var oX = e.event.clientX;

    var oY = e.event.clientY;

    if(oX>140) {        
        oX = 140;
    }
    //菜单出现后的位置
    data.style.display = "block";
    data.style.left = oX + "px";
    //console.log('menu.style.left', menu.style.left)
    data.style.top = oY + "px";
    
 
    //阻止浏览器默认事件
    // return false
}
export default rightClickPosition;