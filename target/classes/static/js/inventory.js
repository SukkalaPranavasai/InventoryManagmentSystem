document.addEventListener("DOMContentLoaded", function () {
    fetchInventory();
});

function fetchInventory() {
    fetch('/api/inventory')
        .then(response => response.json())
        .then(data => {
            const inventoryTable = document.getElementById("inventoryTableBody");
            inventoryTable.innerHTML = "";
            data.forEach(item => {
                const row = `<tr>
                    <td>${item.id}</td>
                    <td>${item.name}</td>
                    <td>${item.sku}</td>
                    <td>${item.category}</td>
                    <td>${item.quantity}</td>
                    <td>${item.costPrice}</td>
                    <td>${item.sellingPrice}</td>
                    <td>
                        <button onclick="editInventory(${item.id})">Edit</button>
                        <button onclick="deleteInventory(${item.id})">Delete</button>
                    </td>
                </tr>`;
                inventoryTable.innerHTML += row;
            });
        })
        .catch(error => console.error("Error fetching inventory:", error));
}

function addInventory() {
    const itemName = document.getElementById("itemName").value;
    const itemQuantity = document.getElementById("itemQuantity").value;
    
    fetch('/api/inventory', {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ name: itemName, quantity: itemQuantity })
    })
    .then(response => response.json())
    .then(() => {
        fetchInventory();
        document.getElementById("itemName").value = "";
        document.getElementById("itemQuantity").value = "";
    })
    .catch(error => console.error("Error adding inventory:", error));
}
