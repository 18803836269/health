/**
 * 获取报告数据
 */
function getBusinessReportData() {
    return axios.get("/report/getBusinessReportData.do").then((response) => {
        return response.data;
    });
}