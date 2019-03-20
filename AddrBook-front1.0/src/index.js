import dva from 'dva';
import './index.less';
// import '../public/config';
// 1. Initialize

const app = dva({});

// 2. Plugins
// app.use({});

// 3. Model
app.model(require('./models/treedata').default);
// app.model(require('./models/Drawer').default);
app.model(require('./models/stateManege').default);
// 4. Router
app.router(require('./router').default);

// 5. Start
app.start('#root');
