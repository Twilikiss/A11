// pages/draws/draws.js
Page({
  /**
   * 页面的初始数据
   */
  data: {
    flag: false,
    flags: false,
    fileUrl: "",
    drawFile: "",
    drawInfo: [],
    id: "",
    num: 0,
    textVal: "",
    taskId: "-1",
    resolution: "1024*1024",
    option4: [
      { text: "1024*1024", value: 0 },
      { text: "1024*1536", value: 1 },
      { text: "1536*1024", value: 2 },
    ],
    value4: 0,
  },
  drwasText(e) {
    this.setData({ textVal: e.detail.value });
  },
  nextKeys() {
    var that = this;
    this.setData({ flags: false });
    wx.showToast({
      title: "正在提取关键词",
      icon: "loading",
      duration: 4000, //持续的时间
    });
    if (!that.data.textVal) {
      this.getFile();
    }

    setTimeout(() => {
      that.setData({
        flag: true,
        flags: true,
      });
    }, 4100);
    wx.request({
      url: `http://www.elysialove.xyz:7701/api/text/nlp/keyword?text=${that.data.textVal}`,
      header: {
        "content-type": "application/json",
        Authorization: wx.getStorageSync("token"),
        RefreshToken: wx.getStorageSync("refresh_token"),
      },
      method: "POST",
      dataType: "json",
      responseType: "text",
      success: (result) => {
        console.log(result);
        if (result.data.code == 200) {
          let text = "";
          for (var i = 0; i < result.data.data.results.length; i++) {
            text += result.data.data.results[i].word + " ";
          }
          that.setData({
            textVal: text,
          });
        }
      },
      fail: () => {},
      complete: () => {},
    });
  },
  nextStyle() {
    var that = this;
    wx.navigateTo({
      url:
        "/pages/style/style?keyWord=" +
        that.data.textVal +
        "&resolution=" +
        that.data.resolution,
    });
  },
  getPlace(e) {
    let s = this.data.option1[e.detail].text;
    this.setData({
      place: s,
    });
  },
  getResolution(e) {
    let s = this.data.option4[e.detail].text;
    this.setData({
      resolution: s,
    });
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    console.log(JSON.parse(options.file));
    // console.log(options);
    this.setData({
      drawFile: JSON.parse(options.file).url,
      id: options.id,
    });
    wx.showToast({
      title: "正在识别中",
      icon: "loading",
      duration: 4000, //持续的时间
    });
    if (this.data.id == 0) {
      this.getFile(`http://www.elysialove.xyz:7701/api/img/ocr/handwriting`);
    } else if (this.data.id == 1) {
      this.getFile(
        `http://www.elysialove.xyz:7701/api/img/ocr/handwriting/auto_error_correction`
      );
      // } else if (this.data.id == 2) {
      //   this.getFile(`http://www.elysialove.xyz:7701/api/img/ocr/accurate_basic`);
    } else if (this.data.id == 2) {
      this.getFile(
        `http://www.elysialove.xyz:7701/api/img/ocr/accurate_basic/auto_error_correction`
      );
    }

    setTimeout(() => {
      this.setData({
        flags: true,
      });
    }, 4100);
  },
  getFile(url) {
    var that = this;
    wx.uploadFile({
      filePath: that.data.drawFile,
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
          console.log(token);
          console.log(refresh_token);
          wx.setStorage({
            key: "token",
            data: token,
          });
          wx.setStorage({
            key: "refresh_token",
            data: refresh_token,
          });
          if (that.data.id == 0) {
            that.getFile(
              `http://www.elysialove.xyz:7701/api/img/ocr/handwriting`
            );
          } else if (that.data.id == 1) {
            that.getFile(
              `http://www.elysialove.xyz:7701/api/img/ocr/handwriting/auto_error_correction`
            );
            // } else if (that.data.id == 2) {
            //   that.getFile(
            //     "http://www.elysialove.xyz:7701/api/img/ocr/accurate_basic?type=image"
            //   );
          } else if (that.data.id == 2) {
            that.getFile(
              `http://www.elysialove.xyz:7701/api/img/ocr/accurate_basic/auto_error_correction`
            );
          }
        } else if (code > 200 && code < 300) {
          wx.showToast({
            title: "请重新登录",
            icon: "error",
            duration: 2000, //持续的时间
          });
          setTimeout(() => {
            wx.navigateTo({
              url: "/pages/login/login",
            });
          }, 2100);
          // console.log(res);
        } else {
          if (that.data.id != 0) {
            that.setData({
              drawInfo: JSON.parse(res.data).data,
              textVal: JSON.parse(res.data).data.results.item.correct_query,
              fileUrl: JSON.parse(res.data).data.url,
            });
          } else {
            var text = "";
            for (var i = 0; i < data.data.results.words_result.length; i++) {
              text += data.data.results.words_result[i].words;
            }
            that.setData({
              textVal: text,
              fileUrl: data.data.url,
            });
          }
        }
      },
    });
  },
  // gettaskId() {
  //   // api/img/ai_painting/base/text2image
  //   var that = this;
  //   wx.request({
  //     url: `http://www.elysialove.xyz:7701/api/img/ai_painting/base/text2image`,
  //     data: {
  //       keyword: that.data.textVal,
  //       num: 1,
  //       otherStyle: [that.data.otherStyle],
  //       placeSelected: that.data.place,
  //       resolution: that.data.resolution,
  //       style: that.data.style,
  //     },
  //     header: {
  //       "content-type": "application/json",
  //       Authorization: wx.getStorageSync("token"),
  //       RefreshToken: wx.getStorageSync("refresh_token"),
  //     },
  //     method: "POST",
  //     dataType: "json",
  //     responseType: "text",
  //     success: (result) => {
  //       console.log(result.data.data.data.taskId);
  //       let taskId = result.data.data.data.taskId;
  //       if (result.data.code == 200) {
  //         wx.navigateTo({
  //           url: "/pages/draws/draws?taskId=" + taskId,
  //         });
  //       }
  //     },
  //     fail: () => {},
  //     complete: () => {},
  //   });
  //   // var taskId = 16505062;
  //   // wx.navigateTo({
  //   //   url: "/pages/draws/draws?taskId=" + taskId,
  //   // });
  // },
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
