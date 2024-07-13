// pages/login/login.js
const app = getApp();
Page({
  data: {
    checked: false,
    flag: true,
    codeVal: "",
    phoneVal: "",
    code: "",
    disabled: false,
    loginCode: 0,
  },
  login() {
    if (this.data.checked) {
      wx.getUserProfile({
        desc: "必须授权才能继续使用", // 必填 声明获取用户个人信息后的用途，后续会展示在弹窗中
        success: (res) => {
          console.log("授权成功", res);
          app.globalData.userInfo = res.userInfo;
          wx.showToast({
            title: "授权成功",
            icon: "loading",
            duration: 2000, //持续的时间
          });
          setTimeout(() => {
            // wx.redirectTo({
            //   url: "/pages/information/information",
            // });
            this.setData({ flag: false });
          }, 2100);
        },
        fail: (err) => {
          console.log("授权失败", err);
          wx.showToast({
            title: "授权失败",
            icon: "error",
            duration: 2000, //持续的时间
          });
        },
      });
    } else {
      wx.showToast({
        title: "请勾选协议",
        icon: "error",
        duration: 2000, //持续的时间
      });
    }
  },
  onChange(event) {
    this.setData({
      checked: event.detail,
    });
  },
  getPhone(e) {
    console.log(e.detail.value);
    this.setData({ phoneVal: e.detail.value });
  },
  setCode() {
    var that = this;
    if (that.data.phoneVal) {
      wx.request({
        url: `http://www.elysialove.xyz:7701/user/auth/sendMessage/${that.data.phoneVal}`,
        header: { "content-type": "application/json" },
        method: "GET",
        dataType: "json",
        responseType: "text",
        success: (result) => {
          console.log(result);
          if (result.data.code == 200) {
            wx.showToast({
              title: "发送成功",
              icon: "success",
              duration: 1000, //持续的时间
            });
            that.setData({
              code: result.data.code,
            });
          }
        },
        fail: () => {},
        complete: () => {},
      });
      this.setData({
        disabled: true,
      });
      setTimeout(() => {
        that.setData({
          disabled: false,
        });
      }, 60000);
    } else {
      wx.showToast({
        title: "请输入手机号！",
        icon: "error",
        duration: 2000, //持续的时间
      });
    }
  },
  getCode(e) {
    console.log(e.detail.value);
    this.setData({ codeVal: e.detail.value });
  },
  login_phone() {
    var that = this;
    if (that.data.checked) {
      if (that.data.code == 200) {
        wx.request({
          url: "http://www.elysialove.xyz:7701/user/auth/login",
          data: {
            code: that.data.codeVal,
            ip: "无",
            openid: "无",
            phone: that.data.phoneVal,
          },
          header: { "Content-Type": "application/json" },
          method: "POST",
          dataType: "json",
          responseType: "text",
          success: (result) => {
            console.log(result);
            //存放token到缓存中
            wx.setStorage({
              key: "token",
              data: result.data.data.access_token,
            });
            wx.setStorage({
              key: "refresh_token",
              data: result.data.data.refresh_token,
            });
            wx.showToast({
              title: "登陆中...",
              icon: "loading",
              duration: 2000, //持续的时间
            });
            // if (that.data.loginCode == 0) {
            //   setTimeout(() => {
            //     wx.redirectTo({
            //       url: "/pages/information/information",
            //     });
            //   }, 2100);
            // } else {
            //   setTimeout(() => {
            //     wx.switchTab({
            //       url: "/pages/index/index",
            //     });
            //   }, 2100);
            // }
            wx.request({
              url: "http://www.elysialove.xyz:7701/user/info/getUserInfo",
              data: {},
              header: {
                "content-type": "application/json",
                Authorization: wx.getStorageSync("token"),
                RefreshToken: wx.getStorageSync("refresh_token"),
              },
              method: "GET",
              dataType: "json",
              responseType: "text",
              success: (result) => {
                console.log(result.data);
                let code = result.data.code;
                if (code == 200 && result.data.data.userIcon == null) {
                  app.globalData.userInfo = result.data.data;
                  console.log(app.globalData.userInfo);
                  that.setData({ loginCode: result.data.code });
                  setTimeout(() => {
                    wx.redirectTo({
                      url: "/pages/information/information",
                    });
                  }, 2100);
                } else {
                  that.setData({ loginCode: result.data.code });
                  setTimeout(() => {
                    wx.switchTab({
                      url: "/pages/index/index",
                    });
                  }, 2100);
                }
              },
              fail: () => {},
              complete: () => {},
            });
          },
          fail: () => {},
          complete: () => {},
        });
      } else {
        wx.showToast({
          title: "未输入信息",
          icon: "error",
          duration: 2000, //持续的时间
        });
      }
    } else {
      wx.showToast({
        title: "请勾选协议",
        icon: "error",
        duration: 2000, //持续的时间
      });
    }
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    var that = this;
    wx.request({
      url: "http://www.elysialove.xyz:7701/user/info/getUserInfo",
      data: {},
      header: {
        "content-type": "application/json",
        Authorization: wx.getStorageSync("token"),
        RefreshToken: wx.getStorageSync("refresh_token"),
      },
      method: "GET",
      dataType: "json",
      responseType: "text",
      success: (result) => {
        console.log(result.data);
        let code = result.data.code;
        if (code == 200 && result.data.data.userIcon != null) {
          app.globalData.userInfo = result.data.data;
          console.log(app.globalData.userInfo);
          that.setData({ loginCode: result.data.code });
          wx.switchTab({
            url: "/pages/index/index",
          });
        } else {
          that.setData({ loginCode: result.data.code });
        }
      },
      fail: () => {},
      complete: () => {},
    });
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {},

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {},

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {},

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {},

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {},

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {},

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {},
});
