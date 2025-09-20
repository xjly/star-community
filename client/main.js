import App from './App'

// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
import request from '@/utils/http.js'
import uView from "uview-ui";
Vue.use(uView);
Vue.prototype.$request = request
Vue.config.productionTip = false

 Vue.prototype.$base_url = 'http://192.168.31.83:8000';

App.mpType = 'app'
const app = new Vue({
	...App
})
app.$mount()
// #endif

// #ifdef VUE3
import {
	createSSRApp
} from 'vue'
export function createApp() {
	const app = createSSRApp(App)
	return {
		app
	}
}
// #endif