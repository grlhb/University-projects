import nose.tools as nt
from pendulum import Pendulum 
import numpy as np 
import numpy.testing

def test_Pendulum():
	"""Checks that the __call__ method returns the correct values."""
	p = Pendulum(2.2)
	nt.assert_almost_equals(0.1,p(0, (np.pi/4, 0.1))[0])
	nt.assert_almost_equals(-3.153,p(0, (np.pi/4, 0.1))[1],places = 3)
	for i in p(0, (0,0)):
		assert i == 0

def test_PendulumSolve():
	"""Checks that the solve method returns the correct values."""
	p = Pendulum(2.2)
	p.solve((0,0), 100, 1)
	expected_t, expected_theta, expected_omega = np.arange(0,100,1), np.zeros(100), np.zeros(100)
	actual_t, actual_theta, actual_omega = p.t, p.theta, p.omega 
	numpy.testing.assert_array_equal(expected_t, actual_t)
	numpy.testing.assert_array_equal(expected_theta, actual_theta)
	numpy.testing.assert_array_equal(expected_omega, actual_omega)

def test_PendulumCartesian():
	"""Checks that the cartesian coordinates are correct."""
	p = Pendulum(2.2)
	p.solve((0.1,np.pi/4),100,0.1)
	for i in range(len(p.x)):
		nt.assert_almost_equals(p.x[i]**2 + p.y[i]**2, 2.2**2)

@nt.raises(AttributeError)
def test_NotSolved():
	"""Checks that t, theta and omega raise and error 
	if the solve method hasn't been called.
	"""
	p = Pendulum(2.2)
	p.t
	p.theta
	p.omega
