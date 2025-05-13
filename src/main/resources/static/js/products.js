document.addEventListener("DOMContentLoaded", function() {
	// Initialize Bootstrap tooltips
	var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
	var tooltipList = tooltipTriggerList.map(function(tooltipTriggerEl) {
		return new bootstrap.Tooltip(tooltipTriggerEl);
	});

	// Sidebar toggle
	document.getElementById('sidebarCollapse').addEventListener('click', function() {
		document.getElementById('sidebar').classList.toggle('active');
	});

	// Clear form when modal is closed
	$('#addProductModal').on('hidden.bs.modal', function() {
		document.getElementById('addProductForm').reset();
		$(this).find('.modal-title').text('Add New Product');
		$(this).find('button[onclick^="updateProduct"]')
			.attr('onclick', 'saveProduct()');
	});
});

// Product CRUD Operations
function saveProduct() {
	// Get form values
	const productName = document.getElementById('productName').value;
	const productSku = document.getElementById('productSku').value;
	const productDescription = document.getElementById('productDescription').value;
	const productCostPrice = document.getElementById('productCostPrice').value;
	const productSellingPrice = document.getElementById('productSellingPrice').value;
	const productPrice = document.getElementById('productPrice').value;
	const productQuantity = document.getElementById('productQuantity').value;
	const productReorderLevel = document.getElementById('productReorderLevel').value;
	
	// Validate required fields
	if (!productName || !productSku || !productDescription || !productPrice || !productQuantity || !productReorderLevel) {
		showAlert('danger', 'Please fill in all required fields');
		return;
	}
	
	// Create product object
	const product = {
		name: productName,
		sku: productSku,
		description: productDescription,
		costPrice: parseFloat(productCostPrice),
		sellingPrice: parseFloat(productSellingPrice),
		price: parseFloat(productPrice),
		quantity: parseInt(productQuantity),
		reorderLevel: parseInt(productReorderLevel)
	};
	
	// Send data to server using fetch API
	fetch('/api/products', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify(product)
	})
	.then(response => {
		if (!response.ok) {
			throw new Error('Failed to save product');
		}
		return response.json();
	})
	.then(data => {
		// Close modal
		const modal = bootstrap.Modal.getInstance(document.getElementById('addProductModal'));
		modal.hide();
		
		// Reset form
		document.getElementById('addProductForm').reset();
		
		// Show success message
		showAlert('success', 'Product saved successfully');
		
		// Reload page to show new product
		setTimeout(() => {
			window.location.reload();
		}, 1000);
	})
	.catch(error => {
		console.error('Error:', error);
		showAlert('danger', error.message || 'Error saving product');
	});
}

function editProduct(id) {
	// Fetch product details
	fetch(`/api/products/${id}`)
		.then(response => {
			if (!response.ok) {
				throw new Error('Failed to load product details');
			}
			return response.json();
		})
		.then(product => {
			// Populate form with product details
			document.getElementById('productName').value = product.name;
			document.getElementById('productSku').value = product.sku;
			document.getElementById('productDescription').value = product.description;
			document.getElementById('productCostPrice').value = product.costPrice;
			document.getElementById('productSellingPrice').value = product.sellingPrice;
			document.getElementById('productPrice').value = product.price;
			document.getElementById('productQuantity').value = product.quantity;
			document.getElementById('productReorderLevel').value = product.reorderLevel;
			
			// Change modal title and save button action
			const modal = document.getElementById('addProductModal');
			modal.querySelector('.modal-title').textContent = 'Edit Product';
			const saveButton = modal.querySelector('.modal-footer .btn-primary');
			saveButton.textContent = 'Update Product';
			saveButton.onclick = function() { updateProduct(id); };
			
			// Show modal
			const bsModal = new bootstrap.Modal(modal);
			bsModal.show();
		})
		.catch(error => {
			console.error('Error:', error);
			showAlert('danger', error.message || 'Error loading product details');
		});
}

function updateProduct(id) {
	// Get form values
	const productName = document.getElementById('productName').value;
	const productSku = document.getElementById('productSku').value;
	const productDescription = document.getElementById('productDescription').value;
	const productCostPrice = document.getElementById('productCostPrice').value;
	const productSellingPrice = document.getElementById('productSellingPrice').value;
	const productPrice = document.getElementById('productPrice').value;
	const productQuantity = document.getElementById('productQuantity').value;
	const productReorderLevel = document.getElementById('productReorderLevel').value;
	
	// Validate required fields
	if (!productName || !productSku || !productDescription || !productPrice || !productQuantity || !productReorderLevel) {
		showAlert('danger', 'Please fill in all required fields');
		return;
	}
	
	// Create product object
	const product = {
		id: id,
		name: productName,
		sku: productSku,
		description: productDescription,
		costPrice: parseFloat(productCostPrice),
		sellingPrice: parseFloat(productSellingPrice),
		price: parseFloat(productPrice),
		quantity: parseInt(productQuantity),
		reorderLevel: parseInt(productReorderLevel)
	};
	
	// Send data to server using fetch API
	fetch(`/api/products/${id}`, {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify(product)
	})
	.then(response => {
		if (!response.ok) {
			throw new Error('Failed to update product');
		}
		return response.json();
	})
	.then(data => {
		// Close modal
		const modal = bootstrap.Modal.getInstance(document.getElementById('addProductModal'));
		modal.hide();
		
		// Reset form
		document.getElementById('addProductForm').reset();
		
		// Show success message
		showAlert('success', 'Product updated successfully');
		
		// Reload page to show updated product
		setTimeout(() => {
			window.location.reload();
		}, 1000);
	})
	.catch(error => {
		console.error('Error:', error);
		showAlert('danger', error.message || 'Error updating product');
	});
}

function deleteProduct(id) {
	// Set the product ID on the confirm button
	document.getElementById("confirmDeleteProductBtn").setAttribute("data-product-id", id);
	
	// Show the modal
	const deleteModal = new bootstrap.Modal(document.getElementById("deleteProductConfirmModal"));
	deleteModal.show();
}

// This function will be called from the confirmation modal
function confirmDeleteProduct() {
	const id = document.getElementById("confirmDeleteProductBtn").getAttribute("data-product-id");
	if (!id) {
		showAlert('danger', 'No product ID found');
		return;
	}
	
	// Close the modal
	const modal = bootstrap.Modal.getInstance(document.getElementById('deleteProductConfirmModal'));
	if (modal) {
		modal.hide();
	}
	
	// Send delete request
	fetch(`/api/products/${id}`, {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json'
		}
	})
	.then(response => {
		if (!response.ok) {
			return response.text().then(text => { throw new Error(text || "Failed to delete product") });
		}
		
		// Show success message
		showAlert('success', 'Product deleted successfully');
		
		// Reload page to show product is removed
		setTimeout(() => {
			window.location.reload();
		}, 1000);
	})
	.catch(error => {
		console.error('Error:', error);
		showAlert('danger', error.message || 'Error deleting product');
	});
}

// Show alert message
function showAlert(type, message) {
	const alertDiv = document.createElement('div');
	alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
	alertDiv.role = 'alert';
	alertDiv.innerHTML = `
		${message}
		<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
	`;
	
	// Remove any existing alerts
	document.querySelectorAll('.alert').forEach(alert => alert.remove());
	
	// Insert the new alert at the top of the content area
	const contentArea = document.querySelector('.content-wrapper');
	contentArea.insertAdjacentElement('afterbegin', alertDiv);
} 