// pages/teletextConverter/teletextConverter.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    file: "",
    fileInfo: "",
    taskId: "",
    creatTime: "",
    endTime: "",
    word: "",
    excel: "",
    flag: false,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {},
  identify(e) {
    let that = this;
    console.log(e.detail.file);
    // console.log(e);
    this.setData({
      file: e.detail.file.url,
    });
    that.getId();
  },
  getId() {
    var that = this;
    wx.uploadFile({
      filePath: that.data.file,
      name: "multipartFile",
      url: `http://www.elysialove.xyz:7701/api/img/ocr/doc_convert/update?type=image`,
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
          that.getId();
        } else if (code > 200 && code < 300) {
          let message = data.message;
          wx.showToast({
            title: message,
            icon: "error",
            duration: 2000, //持续的时间
          });
        } else {
          console.log(data.data.results.result.task_id);
          that.setData({
            taskId: data.data.results.result.task_id,
            flag: true,
          });
          wx.showToast({
            title: "图文转换中",
            icon: "loading",
            image: "",
            duration: 1000,
            mask: false,
            success: (result) => {},
            fail: () => {},
            complete: () => {},
          });

          setTimeout(() => {
            that.getAns();
          }, 1100);
        }
        // 3LzjMhSY
      },
    });
  },
  getAns() {
    var that = this;
    wx.request({
      //GB6ed9pj
      //3LzjMhSY
      url:
        "http://www.elysialove.xyz:7701/api/img/ocr/doc_convert/get?taskId=" +
        that.data.taskId,
      data: {},
      header: {
        "Content-Type": "application/json",
        Authorization: wx.getStorageSync("token"),
        RefreshToken: wx.getStorageSync("refresh_token"),
      },
      method: "POST",
      dataType: "json",
      responseType: "text",
      success: (result) => {
        // console.log(result.data);
        let data = result.data;
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
          that.getId();
        } else if (code > 200 && code < 300) {
          let message = data.message;
          wx.showToast({
            title: message,
            icon: "error",
            duration: 2000, //持续的时间
          });
        } else {
          // console.log(data.data.result);
          that.setData({
            word: data.data.result.result_data.word,
            excel: data.data.result.result_data.excel,
          });
          console.log(that.data.word);
        }
      },
      fail: () => {},
      complete: () => {},
    });
  },
  download() {
    var url = this.data.word;
    wx.setClipboardData({
      data: url,
      success: function (res) {
        // self.setData({copyTip:true}),
        // wx.showModal({
        //   title: "提示",
        //   content: "复制成功",
        //   success: function (res) {
        //     if (res.confirm) {
        //       wx.showToast({
        //         title: '复制成功',
        //         icon: 'success',
        //         image: '',
        //         duration: 1500,
        //         mask: false,
        //         success: (result) => {
        //         },
        //         fail: () => {},
        //         complete: () => {}
        //       });
        //     } else if (res.cancel) {
        //       console.log("取消");
        //     }
        //   },
        // });
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
