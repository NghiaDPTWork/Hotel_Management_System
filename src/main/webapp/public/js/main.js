document.addEventListener('DOMContentLoaded', function () {

    /*========== NAVBAR SCROLL EFFECT ==========*/
    const navbar = document.querySelector('.navbar');
    if (navbar) {
        window.addEventListener('scroll', () => {
            if (window.scrollY > 50) {
                navbar.classList.add('scrolled');
            } else {
                navbar.classList.remove('scrolled');
            }
        });
    }
    /*========== SCROLL ANIMATION ==========*/
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('is-visible');
            }
        });
    }, {
        threshold: 0.1
    });
    const elementsToAnimate = document.querySelectorAll('.scroll-animate');
    elementsToAnimate.forEach(el => observer.observe(el));

    /*========== ROOM DETAIL MODAL ==========*/
    const roomDetailModal = document.getElementById('roomDetailModal');
    if (roomDetailModal) {
        roomDetailModal.addEventListener('show.bs.modal', function (event) {
            const button = event.relatedTarget;
            const roomId = button.getAttribute('data-room-id');
            const roomName = button.getAttribute('data-room-name');
            const roomPrice = button.getAttribute('data-room-price');
            const roomImage = button.getAttribute('data-room-image');

            const roomCardBody = button.closest('.card-body');
            const roomDescription = roomCardBody.querySelector('.card-text').textContent;
            const roomAmenitiesHTML = roomCardBody.querySelector('.room-amenities-list').innerHTML;

            const modal = this;
            const modalBody = modal.querySelector('.modal-body');

            modal.querySelector('#modalRoomTitle').textContent = roomName;
            modalBody.innerHTML = `
                <div class="row">
                    <div class="col-md-6">
                        <img src="${roomImage}" class="img-fluid rounded" alt="Room Image">
                    </div>
                    <div class="col-md-6">
                        <p class="text-muted">${roomDescription}</p>
                        <h5>Amenities</h5>
                        <ul class="room-amenities-list">
                            ${roomAmenitiesHTML}
                        </ul>
                        <div class="mt-3">
                            <p class="room-price mb-0 fs-4">${roomPrice}</p>
                        </div>
                    </div>
                </div>
            `;
            
            modal.querySelector('#bookNowBtn').dataset.roomId = roomId;
        });

        const bookNowBtn = document.getElementById('bookNowBtn');
        bookNowBtn.addEventListener('click', function () {
            const roomIdToBook = this.dataset.roomId;
            const roomTypeSelect = document.getElementById('roomType');
            
            if (roomTypeSelect && roomIdToBook) {
                roomTypeSelect.value = roomIdToBook;
            }

            const modalInstance = bootstrap.Modal.getInstance(roomDetailModal);
            modalInstance.hide();
            
            const mainBookingForm = document.getElementById('bookingForm');
            const mainSubmitButton = mainBookingForm.querySelector('button[type="submit"]');

            if (mainSubmitButton) {
                mainSubmitButton.click();
            } else {
                const loginButton = mainBookingForm.querySelector('button[data-bs-target="#loginModal"]');
                if (loginButton) {
                    loginButton.click();
                }
            }
        });
    }
});