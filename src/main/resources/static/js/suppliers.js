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
	$('#addSupplierModal').on('hidden.bs.modal', function() {
		document.getElementById('addSupplierForm').reset();
		$(this).find('.modal-title').text('Add New Supplier');
		$(this).find('button[onclick^="updateSupplier"]')
			.attr('onclick', 'saveSupplier()');
	});
});

// Supplier CRUD Operations
function saveSupplier() {
	// Get form values
	const supplierName = document.getElementById('supplierName').value;
	const supplierEmail = document.getElementById('supplierEmail').value;
	const supplierPhone = document.getElementById('supplierPhone').value;
	const supplierAddress = document.getElementById('supplierAddress').value;
	
	// Validate required fields
	if (!supplierName || !supplierEmail || !supplierPhone || !supplierAddress) {
		showAlert('danger', 'Please fill in all required fields');
		return;
	}
	
	// Create supplier object
	const supplier = {
		name: supplierName,
		email: supplierEmail,
		phone: supplierPhone,
		address: supplierAddress
	};
	
	// Send data to server using fetch API
	fetch('/api/suppliers', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify(supplier)
	})
	.then(response => {
		if (!response.ok) {
			throw new Error('Failed to save supplier');
		}
		return response.json();
	})
	.then(data => {
		// Close modal
		const modal = bootstrap.Modal.getInstance(document.getElementById('addSupplierModal'));
		modal.hide();
		
		// Reset form
		document.getElementById('addSupplierForm').reset();
		
		// Show success message
		showAlert('success', 'Supplier saved successfully');
		
		// Reload page to show new supplier
		setTimeout(() => {
			window.location.reload();
		}, 1000);
	})
	.catch(error => {
		console.error('Error:', error);
		showAlert('danger', error.message || 'Error saving supplier');
	});
}

function editSupplier(id) {
	// Fetch supplier details
	fetch(`/api/suppliers/${id}`)
		.then(response => {
			if (!response.ok) {
				throw new Error('Failed to load supplier details');
			}
			return response.json();
		})
		.then(supplier => {
			// Populate form with supplier details
			document.getElementById('supplierName').value = supplier.name;
			document.getElementById('supplierEmail').value = supplier.email;
			document.getElementById('supplierPhone').value = supplier.phone;
			document.getElementById('supplierAddress').value = supplier.address;
			
			// Change modal title and save button action
			const modal = document.getElementById('addSupplierModal');
			modal.querySelector('.modal-title').textContent = 'Edit Supplier';
			const saveButton = modal.querySelector('.modal-footer .btn-primary');
			saveButton.textContent = 'Update Supplier';
			saveButton.onclick = function() { updateSupplier(id); };
			
			// Show modal
			const bsModal = new bootstrap.Modal(modal);
			bsModal.show();
		})
		.catch(error => {
			console.error('Error:', error);
			showAlert('danger', error.message || 'Error loading supplier details');
		});
}

function updateSupplier(id) {
	// Get form values
	const supplierName = document.getElementById('supplierName').value;
	const supplierEmail = document.getElementById('supplierEmail').value;
	const supplierPhone = document.getElementById('supplierPhone').value;
	const supplierAddress = document.getElementById('supplierAddress').value;
	
	// Validate required fields
	if (!supplierName || !supplierEmail || !supplierPhone || !supplierAddress) {
		showAlert('danger', 'Please fill in all required fields');
		return;
	}
	
	// Create supplier object
	const supplier = {
		id: id,
		name: supplierName,
		email: supplierEmail,
		phone: supplierPhone,
		address: supplierAddress
	};
	
	// Send data to server using fetch API
	fetch(`/api/suppliers/${id}`, {
		method: 'PUT',
		headers: {
			'Content-Type': 'application/json',
		},
		body: JSON.stringify(supplier)
	})
	.then(response => {
		if (!response.ok) {
			throw new Error('Failed to update supplier');
		}
		return response.json();
	})
	.then(data => {
		// Close modal
		const modal = bootstrap.Modal.getInstance(document.getElementById('addSupplierModal'));
		modal.hide();
		
		// Reset form
		document.getElementById('addSupplierForm').reset();
		
		// Show success message
		showAlert('success', 'Supplier updated successfully');
		
		// Reload page to show updated supplier
		setTimeout(() => {
			window.location.reload();
		}, 1000);
	})
	.catch(error => {
		console.error('Error:', error);
		showAlert('danger', error.message || 'Error updating supplier');
	});
}

function deleteSupplier(id) {
	// Set the supplier ID on the confirm button
	document.getElementById("confirmDeleteSupplierBtn").setAttribute("data-supplier-id", id);
	
	// Show the modal
	const deleteModal = new bootstrap.Modal(document.getElementById("deleteSupplierConfirmModal"));
	deleteModal.show();
}

// This function will be called from the confirmation modal
function confirmDeleteSupplier() {
	const id = document.getElementById("confirmDeleteSupplierBtn").getAttribute("data-supplier-id");
	if (!id) {
		showAlert('danger', 'No supplier ID found');
		return;
	}
	
	// Close the modal
	const modal = bootstrap.Modal.getInstance(document.getElementById('deleteSupplierConfirmModal'));
	if (modal) {
		modal.hide();
	}
	
	// Send delete request
	fetch(`/api/suppliers/${id}`, {
		method: 'DELETE',
		headers: {
			'Content-Type': 'application/json'
		}
	})
	.then(response => {
		if (!response.ok) {
			return response.text().then(text => { throw new Error(text || "Failed to delete supplier") });
		}
		
		// Show success message
		showAlert('success', 'Supplier deleted successfully');
		
		// Reload page to show supplier is removed
		setTimeout(() => {
			window.location.reload();
		}, 1000);
	})
	.catch(error => {
		console.error('Error:', error);
		showAlert('danger', error.message || 'Error deleting supplier');
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