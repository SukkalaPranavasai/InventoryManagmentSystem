document.addEventListener("DOMContentLoaded", function () {
    // Initialize sidebar toggle functionality
    document.getElementById("sidebarCollapse").addEventListener("click", function () {
        document.getElementById("sidebar").classList.toggle("active");
    });

    loadOrders();

    // Set up event listener for the place order button in the modal
    document.getElementById("placeOrderBtn").addEventListener("click", () => {
        const productId = document.getElementById("productId").value;
        const quantity = document.getElementById("quantity").value;

        if (!productId || !quantity) {
            showToast("Please fill in all fields", "danger");
            return;
        }

        fetch("/api/orders", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ productId, quantity })
        })
        .then(res => {
            if (!res.ok) {
                return res.text().then(text => { throw new Error(text) });
            }
            return res.json();
        })
        .then((data) => {
            // Close the modal after successful order placement
            const modal = bootstrap.Modal.getInstance(document.getElementById('addOrderModal'));
            modal.hide();
            
            // Reset the form
            document.getElementById("addOrderForm").reset();
            
            showToast("Order placed successfully!");
            loadOrders();
        })
        .catch((error) => {
            console.error("Error:", error);
            showToast(error.message || "Failed to place order!", "danger");
        });
    });

    // Set up the delete confirmation modal button
    document.getElementById("confirmDeleteBtn").addEventListener("click", function() {
        const orderId = this.getAttribute("data-order-id");
        if (orderId) {
            deleteOrder(orderId);
        }
    });

    // Set up the edit button in the update order modal
    document.getElementById("updateOrderBtn").addEventListener("click", function() {
        const orderId = this.getAttribute("data-order-id");
        if (orderId) {
            updateOrder(orderId);
        }
    });
});

function loadOrders() {
    fetch("/api/orders")
        .then(res => {
            if (!res.ok) {
                throw new Error("Failed to load orders");
            }
            return res.json();
        })
        .then(orders => {
            const tbody = document.getElementById("ordersTableBody");
            tbody.innerHTML = "";
            
            if (orders.length === 0) {
                tbody.innerHTML = `<tr><td colspan="5" class="text-center">No orders found. Place your first order!</td></tr>`;
                return;
            }
            
            // First, get all order items to display with orders
            fetch("/api/orderitems")
                .then(res => res.json())
                .then(orderItems => {
                    // Create a map to group order items by order ID
                    const orderItemsMap = new Map();
                    orderItems.forEach(item => {
                        if (!orderItemsMap.has(item.order.id)) {
                            orderItemsMap.set(item.order.id, []);
                        }
                        orderItemsMap.get(item.order.id).push(item);
                    });
                    
                    // Display orders with their items
                    orders.forEach(order => {
                        const items = orderItemsMap.get(order.id) || [];
                        let productInfo = "No products";
                        
                        if (items.length > 0) {
                            productInfo = items.map(item => 
                                `${item.product ? item.product.name : 'Unknown'} (${item.quantity})`
                            ).join(', ');
                        }
                        
                        const row = `<tr>
                            <td>${order.id}</td>
                            <td>${order.orderNumber}</td>
                            <td>${productInfo}</td>
                            <td>${order.totalAmount ? '$' + order.totalAmount.toFixed(2) : '$0.00'}</td>
                            <td>
                                <button class="btn btn-warning btn-sm me-2" onclick="editOrder(${order.id})" 
                                    title="Edit Order" aria-label="Edit Order">
                                    <i class="fas fa-edit" aria-hidden="true"></i>
                                    <span class="visually-hidden">Edit</span>
                                </button>
                                <button class="btn btn-danger btn-sm" onclick="confirmDelete(${order.id})" 
                                    title="Delete Order" aria-label="Delete Order">
                                    <i class="fas fa-trash" aria-hidden="true"></i>
                                    <span class="visually-hidden">Delete</span>
                                </button>
                            </td>
                        </tr>`;
                        tbody.insertAdjacentHTML("beforeend", row);
                    });
                })
                .catch(error => {
                    console.error("Error loading order items:", error);
                    // Still show orders even if order items fail to load
                    orders.forEach(order => {
                        const row = `<tr>
                            <td>${order.id}</td>
                            <td>${order.orderNumber}</td>
                            <td>Unable to load products</td>
                            <td>${order.totalAmount ? '$' + order.totalAmount.toFixed(2) : '$0.00'}</td>
                            <td>
                                <button class="btn btn-warning btn-sm me-2" onclick="editOrder(${order.id})" 
                                    title="Edit Order" aria-label="Edit Order">
                                    <i class="fas fa-edit" aria-hidden="true"></i>
                                    <span class="visually-hidden">Edit</span>
                                </button>
                                <button class="btn btn-danger btn-sm" onclick="confirmDelete(${order.id})" 
                                    title="Delete Order" aria-label="Delete Order">
                                    <i class="fas fa-trash" aria-hidden="true"></i>
                                    <span class="visually-hidden">Delete</span>
                                </button>
                            </td>
                        </tr>`;
                        tbody.insertAdjacentHTML("beforeend", row);
                    });
                });
        })
        .catch(error => {
            console.error("Error loading orders:", error);
            showToast("Failed to load orders", "danger");
            
            const tbody = document.getElementById("ordersTableBody");
            tbody.innerHTML = `<tr><td colspan="5" class="text-center">Error loading orders: ${error.message}</td></tr>`;
        });
}

function editOrder(id) {
    // Fetch order details
    fetch(`/api/orders/${id}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Failed to load order details");
            }
            return response.json();
        })
        .then(order => {
            // Also fetch order items
            fetch(`/api/orderitems`)
                .then(res => res.json())
                .then(allItems => {
                    // Find items for this order
                    const orderItems = allItems.filter(item => item.order.id === order.id);
                    
                    if (orderItems.length > 0) {
                        const item = orderItems[0]; // Get the first item
                        
                        // Populate the edit form
                        document.getElementById("editProductId").value = item.product.id;
                        document.getElementById("editQuantity").value = item.quantity;
                        
                        // Set order ID on update button
                        document.getElementById("updateOrderBtn").setAttribute("data-order-id", order.id);
                        
                        // Show the edit modal
                        const editModal = new bootstrap.Modal(document.getElementById("editOrderModal"));
                        editModal.show();
                    } else {
                        showToast("Unable to find order items", "danger");
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                    showToast("Failed to load order items", "danger");
                });
        })
        .catch(error => {
            console.error("Error:", error);
            showToast("Failed to load order details", "danger");
        });
}

function updateOrder(id) {
    const productId = document.getElementById("editProductId").value;
    const quantity = document.getElementById("editQuantity").value;
    
    if (!productId || !quantity) {
        showToast("Please fill in all fields", "danger");
        return;
    }
    
    fetch(`/api/orders/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ productId, quantity })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to update order");
        }
        
        // Close the modal
        const modal = bootstrap.Modal.getInstance(document.getElementById("editOrderModal"));
        modal.hide();
        
        showToast("Order updated successfully");
        loadOrders();
    })
    .catch(error => {
        console.error("Error:", error);
        showToast("Failed to update order: " + error.message, "danger");
    });
}

function confirmDelete(id) {
    // Set the order ID on the confirm button
    document.getElementById("confirmDeleteBtn").setAttribute("data-order-id", id);
    
    // Show the modal
    const deleteModal = new bootstrap.Modal(document.getElementById("deleteConfirmModal"));
    deleteModal.show();
}

function deleteOrder(id) {
    // First, hide the modal
    const modal = bootstrap.Modal.getInstance(document.getElementById('deleteConfirmModal'));
    if (modal) {
        modal.hide();
    }
    
    // Then send the delete request
    fetch(`/api/orders/${id}`, { 
        method: "DELETE",
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => { 
                throw new Error(text || "Failed to delete order"); 
            });
        }
        
        showToast("Order deleted successfully!", "success");
        // Reload the orders list
        setTimeout(() => {
            loadOrders();
        }, 500);
    })
    .catch(error => {
        console.error("Error:", error);
        showToast(error.message || "Failed to delete order!", "danger");
    });
}

function showToast(message, type = 'success') {
    const toast = document.getElementById('orderToast');
    const msgEl = document.getElementById('toastMsg');
    toast.className = `toast align-items-center text-white bg-${type} border-0`;
    msgEl.textContent = message;
    new bootstrap.Toast(toast).show();
}
