// pages/identifys/identifys.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    fileInfo: [],
    flag: false,
    file: "",
    id: "",
    text: "",
    url: "",
  },
  out() {
    wx.navigateTo({
      url:
        "/pages/out/out?baikeUrl=" + this.data.fileInfo[0].baike_info.baike_url,
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    // console.log(JSON.parse(options.file));
    console.log(JSON.parse(options.file));
    console.log(options.id);
    this.setData({ file: JSON.parse(options.file).url, id: options.id });
    var that = this;
    wx.showToast({
      title: "正在识别中",
      icon: "loading",
      duration: 4000, //持续的时间
    });
    if (this.data.id == 0) {
      this.identify(
        `http://www.elysialove.xyz:7701/api/img/image_classify/animal?baikeNum=1`
      );
    } else if (this.data.id == 1) {
      this.identify(
        `http://www.elysialove.xyz:7701/api/img/image_classify/car?baikeNum=1`
      );
    } else if (this.data.id == 2) {
      this.identifys(
        `http://www.elysialove.xyz:7701/api/img/image_classify/currency`
      );
    } else if (this.data.id == 3) {
      this.identify(
        `http://www.elysialove.xyz:7701/api/img/image_classify/dishes?baikeNum=1`
      );
    } else if (this.data.id == 4) {
      this.identify(
        `http://www.elysialove.xyz:7701/api/img/image_classify/ingredient`
      );
    } else if (this.data.id == 5) {
      this.identifys(
        `http://www.elysialove.xyz:7701/api/img/image_classify/landmark`
      );
    } else if (this.data.id == 6) {
      this.identify(
        `http://www.elysialove.xyz:7701/api/img/image_classify/logo`
      );
    } else {
      this.identify(
        `http://www.elysialove.xyz:7701/api/img/image_classify/plant?baikeNum=1`
      );
    }
    setTimeout(() => {
      that.setData({ flag: true });
    }, 4100);
  },
  identify(url) {
    var that = this;
    wx.uploadFile({
      filePath: that.data.file,
      name: "multipartFile",
      url: url,
      header: {
        "Content-Type": "application/json",
        // accept: "application/json",
        Authorization: wx.getStorageSync("token"),
        RefreshToken: wx.getStorageSync("refresh_token"),
      },
      formData: {},
      success(res) {
        console.log(JSON.parse(res.data));
        let data = JSON.parse(res.data);
        let code = data.code;
        if (code > 400) {
          let arr = data.message.split(";");
          let token = arr[0].substring(13);
          let refresh_token = arr[1].substring(14);
          wx.setStorage({
            key: "token",
            data: token,
          });
          wx.setStorage({
            key: "refresh_token",
            data: refresh_token,
          });
          if (this.data.id == 0) {
            this.identify(
              `http://www.elysialove.xyz:7701/api/img/image_classify/animal?baikeNum=1`
            );
          } else if (this.data.id == 1) {
            this.identify(
              `http://www.elysialove.xyz:7701/api/img/image_classify/car?baikeNum=1`
            );
          } else if (this.data.id == 2) {
            this.identifys(
              `http://www.elysialove.xyz:7701/api/img/image_classify/currency`
            );
          } else if (this.data.id == 3) {
            this.identify(
              `http://www.elysialove.xyz:7701/api/img/image_classify/dishes?baikeNum=1`
            );
          } else if (this.data.id == 4) {
            this.identify(
              `http://www.elysialove.xyz:7701/api/img/image_classify/ingredient`
            );
          } else if (this.data.id == 5) {
            this.identifys(
              `http://www.elysialove.xyz:7701/api/img/image_classify/landmark`
            );
          } else if (this.data.id == 6) {
            this.identify(
              `http://www.elysialove.xyz:7701/api/img/image_classify/logo`
            );
          } else {
            this.identify(
              `http://www.elysialove.xyz:7701/api/img/image_classify/plant?baikeNum=1`
            );
          }
        } else if (code > 200 && code < 300) {
          let message = data.message;
          wx.showToast({
            title: message,
            icon: "error",
            duration: 2000, //持续的时间
          });
          setTimeout(() => {
            wx.navigateTo({
              url: "/pages/login/login",
            });
          }, 2100);
        } else {
          that.setData({ fileInfo: data.data.results.result });
        }
      },
    });
  },
  identifys(url) {
    var that = this;
    wx.uploadFile({
      filePath: that.data.file,
      name: "multipartFile",
      url: url,
      header: {
        "Content-Type": "application/json",
        // accept: "application/json",
        Authorization: wx.getStorageSync("token"),
        RefreshToken: wx.getStorageSync("refresh_token"),
      },
      formData: {},
      success(res) {
        console.log(JSON.parse(res.data));
        let data = JSON.parse(res.data);
        let code = data.code;
        if (code > 400) {
          let arr = data.message.split(";");
          let token = arr[0].substring(13);
          let refresh_token = arr[1].substring(14);
          wx.setStorage({
            key: "token",
            data: token,
          });
          wx.setStorage({
            key: "refresh_token",
            data: refresh_token,
          });
          if (this.data.id == 0) {
            this.identify(
              `http://www.elysialove.xyz:7701/api/img/image_classify/animal?baikeNum=1`
            );
          } else if (this.data.id == 1) {
            this.identify(
              `http://www.elysialove.xyz:7701/api/img/image_classify/car?baikeNum=1`
            );
          } else if (this.data.id == 2) {
            this.identifys(
              `http://www.elysialove.xyz:7701/api/img/image_classify/currency`
            );
          } else if (this.data.id == 3) {
            this.identify(
              `http://www.elysialove.xyz:7701/api/img/image_classify/dishes?baikeNum=1`
            );
          } else if (this.data.id == 4) {
            this.identify(
              `http://www.elysialove.xyz:7701/api/img/image_classify/ingredient`
            );
          } else if (this.data.id == 5) {
            this.identifys(
              `http://www.elysialove.xyz:7701/api/img/image_classify/landmark`
            );
          } else if (this.data.id == 6) {
            this.identify(
              `http://www.elysialove.xyz:7701/api/img/image_classify/logo`
            );
          } else {
            this.identify(
              `http://www.elysialove.xyz:7701/api/img/image_classify/plant?baikeNum=1`
            );
          }
        } else if (code > 200 && code < 300) {
          let message = data.message;
          wx.showToast({
            title: message,
            icon: "error",
            duration: 2000, //持续的时间
          });
          setTimeout(() => {
            wx.navigateTo({
              url: "/pages/login/login",
            });
          }, 2100);
        } else {
          // that.setData({ fileInfo: [data.data.results.result.currencyName] });
          // console.log(res);
          if (that.data.id == 2) {
            that.setData({ text: data.data.results.result.currencyName });
          } else if ((that.data.id = 5)) {
            that.setData({ text: data.data.results.result.landmark });
          }
        }
      },
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
