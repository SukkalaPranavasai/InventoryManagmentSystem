document.addEventListener("DOMContentLoaded", function() {
	generateInventoryReport();
});

function generateInventoryReport() {
	fetch('/api/reports/inventory')
		.then(response => response.json())
		.then(data => {
			const reportTable = document.getElementById("reportTableBody");
			reportTable.innerHTML = "";
			data.forEach(report => {
				const row = `<tr>
                    <td>${report.productName}</td>
                    <td>${report.categoryName}</td>
                    <td>${report.quantity}</td>
                    <td>${report.costPrice}</td>
                    <td>${report.totalValue}</td>
                </tr>`;
				reportTable.innerHTML += row;
			});
		})
		.catch(error => console.error("Error fetching reports:", error));
}

function generateLowStockReport() {
	fetch('/api/reports/low-stock')
		.then(response => response.json())
		.then(data => {
			const lowStockTable = document.getElementById("lowStockTableBody");
			lowStockTable.innerHTML = "";
			data.forEach(report => {
				const row = `<tr>
                    <td>${report.productName}</td>
                    <td>${report.categoryName}</td>
                    <td>${report.quantity}</td>
                    <td>${report.reorderLevel}</td>
                    <td>${report.supplierName}</td>
                </tr>`;
				lowStockTable.innerHTML += row;
			});
		})
		.catch(error => console.error("Error fetching low stock report:", error));
}
