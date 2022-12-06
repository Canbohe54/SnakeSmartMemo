<template>
  <div id="app">
    <el-header>
      <el-row>
        <el-col> </el-col>
        <el-col>
          <el-menu
            default-active="notepad"
            class="el-menu-demo"
            mode="horizontal"
            router
          >
            <el-menu-item index="notepad" route="/" class="notepad"
              >笔记本</el-menu-item
            >
            <el-menu-item index="login" route="/login" class="loginAndReg"
              >登录/注册</el-menu-item
            >
          </el-menu>
        </el-col>
      </el-row>
    </el-header>
    <el-main>
          <router-view />
    </el-main>

  </div>
</template>
<script>
</script>
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
.notepad {
  margin: 0 0 0 120px !important;
}
.loginAndReg {
  float: right !important;
  margin: 0 20px 0 0 !important;
}
</style>
