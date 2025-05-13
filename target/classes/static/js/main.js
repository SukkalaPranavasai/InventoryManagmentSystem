$(document).ready(function () {
    // Sidebar toggle
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });

    // Close sidebar on small screens when clicking outside
    $(document).on('click', function (e) {
        if ($(window).width() <= 768) {
            if (!$(e.target).closest('#sidebar, #sidebarCollapse').length) {
                $('#sidebar').addClass('active');
            }
        }
    });

    // Add hover effect to cards
    $('.card').hover(
        function() { $(this).addClass('shadow-lg'); },
        function() { $(this).removeClass('shadow-lg'); }
    );

    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl)
    });

    // Handle order form submission
    $('#addOrderForm').on('submit', function(e) {
        e.preventDefault();
        
        const formData = {
            orderNumber: $('#orderNumber').val(),
            totalAmount: $('#totalAmount').val()
        };

        $.ajax({
            url: $(this).attr('action'),
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(formData),
            success: function(response) {
                $('#addOrderModal').modal('hide');
                showAlert('success', 'Order created successfully!');
                setTimeout(() => window.location.reload(), 1000);
            },
            error: function(xhr, status, error) {
                showAlert('danger', 'Error creating order: ' + error);
            }
        });
    });

    // Handle order deletion
    $('.btn-danger').on('click', function() {
        const orderId = $(this).data('order-id');
        
        // Create and show delete confirmation modal
        const modalHtml = `
            <div class="modal fade" id="deleteConfirmModal" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Confirm Delete</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            Are you sure you want to delete this order?
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <button type="button" class="btn btn-danger confirm-delete" data-id="${orderId}">Delete</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
        
        // Remove any existing modal
        $('#deleteConfirmModal').remove();
        
        // Add the modal to the document
        $('body').append(modalHtml);
        
        // Show the modal
        $('#deleteConfirmModal').modal('show');
        
        // Handle delete confirmation
        $('.confirm-delete').on('click', function() {
            const id = $(this).data('id');
            $.ajax({
                url: '/orders/' + id,
                type: 'DELETE',
                success: function() {
                    $('#deleteConfirmModal').modal('hide');
                    showAlert('success', 'Order deleted successfully!');
                    setTimeout(() => window.location.reload(), 1000);
                },
                error: function(xhr, status, error) {
                    $('#deleteConfirmModal').modal('hide');
                    showAlert('danger', 'Error deleting order: ' + error);
                }
            });
        });
    });

    // Handle order status update
    $('.status-update').on('change', function() {
        const orderId = $(this).data('order-id');
        const newStatus = $(this).val();
        
        $.ajax({
            url: '/orders/' + orderId + '/status',
            type: 'POST',
            data: { status: newStatus },
            success: function() {
                showAlert('success', 'Order status updated successfully!');
                setTimeout(() => window.location.reload(), 1000);
            },
            error: function(xhr, status, error) {
                showAlert('danger', 'Error updating order status: ' + error);
            }
        });
    });
});

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