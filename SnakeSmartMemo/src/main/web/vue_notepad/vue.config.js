const { defineConfig } = require('@vue/cli-service')
module.exports = {
  transpileDependencies: true,
  runtimeCompiler: true,
  chainWebpack: config => {
    config
      .plugin('html')
      .tap(args => {
        args[0].title= '蛇蛇智能备忘录'
        return args
      })
  },
}