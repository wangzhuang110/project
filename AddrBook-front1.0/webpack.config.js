module.exports = function (webpackConfig, env) {
  // 对roadhog默认配置进行操作，比如：
    if(process.env.NODE_ENV === 'production') {
      webpackConfig.output.publicPath = "/LK-0100004/";
    }
 
    // 添加worker-loader解析
    webpackConfig.module.rules.push({
      test: /\.webworker\.js$/,
      use: { loader: 'worker-loader' }
    });
  
    // 添加shared worker解析
    webpackConfig.module.rules.push({
      test: /\.sharedworker\.js$/,
      use: { loader: 'sharedworker-loader' }
    });
    
  return webpackConfig;
}